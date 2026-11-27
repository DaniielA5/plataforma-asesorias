package com.asesorias.asesorias;

import com.asesorias.asesoria.Asesoria;
import com.asesorias.asesoria.AsesoriaPeticion;
import com.asesorias.asesoria.AsesoriaRepository;
import com.asesorias.asesoria.AsesoriaRespuesta;
import com.asesorias.asesoria.AsesoriaService;
import com.asesorias.asesoria.EstadoAsesoria;
import com.asesorias.error.ReglaNegocioException;
import com.asesorias.inscripcion.Inscripcion;
import com.asesorias.inscripcion.InscripcionRepository;
import com.asesorias.materia.Materia;
import com.asesorias.materia.MateriaRepository;
import com.asesorias.notificacion.AvisoPorCorreo;
import com.asesorias.notificacion.NotificacionService;
import com.asesorias.notificacion.TipoAviso;
import com.asesorias.usuario.Usuario;
import com.asesorias.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsesoriasServiceTest {
 
    private static final long PROFESOR_ID = 1L;
    private static final long ASESORIA_ID = 10L;

    @Mock AsesoriaRepository asesorias;
    @Mock InscripcionRepository inscripciones;
    @Mock MateriaRepository materias;
    @Mock UsuarioRepository usuarios;
    @Mock NotificacionService notificaciones;
    @Mock AvisoPorCorreo avisoCorreo;

    @InjectMocks AsesoriaService servicio;

    private final Materia calculo = materia(5L, "Cálculo integral");
    private final Materia fisica = materia(6L, "Física");

    @Test
    void noPermiteCrearUnaAsesoriaDeUnaMateriaQueNoImparte() {
        when(materias.findById(6L)).thenReturn(Optional.of(fisica));
        when(usuarios.existsByIdAndMateriasImpartidasId(PROFESOR_ID, 6L)).thenReturn(false);

        assertThrows(ReglaNegocioException.class, () -> servicio.crear(PROFESOR_ID, peticion(6L, 5)));

        verify(asesorias, never()).save(any());
    }

    @Test
    void noPermiteCancelarDosVecesNiReenviarLosAvisos() {
        Asesoria yaCancelada = asesoria(EstadoAsesoria.CANCELADA);
        when(asesorias.findById(ASESORIA_ID)).thenReturn(Optional.of(yaCancelada));

        assertThrows(ReglaNegocioException.class, () -> servicio.cancelar(PROFESOR_ID, ASESORIA_ID));

        verify(notificaciones, never()).enviarWhatsappConPlantilla(any(), any(), anyList(), anyString());
        verify(avisoCorreo, never()).avisar(any(), anyString(), anyString());
    }

    @Test
    void alCancelarUnaActivaAvisaPorWhatsappACadaInscrito() {
        Asesoria activa = asesoria(EstadoAsesoria.ACTIVA);
        when(asesorias.findById(ASESORIA_ID)).thenReturn(Optional.of(activa));
        when(inscripciones.findByAsesoriaId(ASESORIA_ID)).thenReturn(List.of(inscripcion(activa), inscripcion(activa)));

        AsesoriaRespuesta respuesta = servicio.cancelar(PROFESOR_ID, ASESORIA_ID);

        assertEquals(EstadoAsesoria.CANCELADA, respuesta.estado());
        verify(notificaciones, times(2)).enviarWhatsappConPlantilla(
                any(), eq(TipoAviso.CANCELACION_ASESORIA), anyList(), anyString());
        verify(avisoCorreo, times(2)).avisar(any(), anyString(), anyString());
    }

    @Test
    void noPermiteBajarElCupoPorDebajoDeLosInscritos() {
        Asesoria activa = asesoria(EstadoAsesoria.ACTIVA);
        when(asesorias.findById(ASESORIA_ID)).thenReturn(Optional.of(activa));
        when(materias.findById(5L)).thenReturn(Optional.of(calculo));
        when(inscripciones.countByAsesoriaId(ASESORIA_ID)).thenReturn(3L);

        assertThrows(ReglaNegocioException.class, () -> servicio.editar(PROFESOR_ID, ASESORIA_ID, peticion(5L, 2)));
    }

    @Test
    void noPermiteEditarUnaAsesoriaCancelada() {
        when(asesorias.findById(ASESORIA_ID)).thenReturn(Optional.of(asesoria(EstadoAsesoria.CANCELADA)));

        assertThrows(ReglaNegocioException.class, () -> servicio.editar(PROFESOR_ID, ASESORIA_ID, peticion(5L, 8)));
    }

    @Test
    void permiteEditarElLugarSinVolverAPedirLaMateria() {
        Asesoria activa = asesoria(EstadoAsesoria.ACTIVA);
        when(asesorias.findById(ASESORIA_ID)).thenReturn(Optional.of(activa));
        when(materias.findById(5L)).thenReturn(Optional.of(calculo));
        when(inscripciones.countByAsesoriaId(ASESORIA_ID)).thenReturn(2L);

        AsesoriaRespuesta respuesta = servicio.editar(PROFESOR_ID, ASESORIA_ID, peticion(5L, 2));

        assertEquals("Aula 9", respuesta.lugar());
        verify(usuarios, never()).existsByIdAndMateriasImpartidasId(any(), any());
    }

    // ---- ayudas para armar datos ----

    private Asesoria asesoria(EstadoAsesoria estado) {
        Usuario profesor = new Usuario();
        profesor.setId(PROFESOR_ID);
        profesor.setNombre("Aracely Ramirez");

        Asesoria a = new Asesoria();
        a.setId(ASESORIA_ID);
        a.setProfesor(profesor);
        a.setMateria(calculo);
        a.setEstado(estado);
        a.setFecha(LocalDate.now().plusDays(1));
        a.setHora(LocalTime.of(10, 0));
        a.setLugar("Aula B-204");
        a.setCupoMax(6);
        return a;
    }

    private static Inscripcion inscripcion(Asesoria asesoria) {
        Usuario alumno = new Usuario();
        alumno.setNombre("Ana Lopez");

        Inscripcion i = new Inscripcion();
        i.setAsesoria(asesoria);
        i.setAlumno(alumno);
        return i;
    }

    private static Materia materia(Long id, String nombre) {
        Materia m = new Materia();
        m.setId(id);
        m.setNombre(nombre);
        return m;
    }

    private static AsesoriaPeticion peticion(Long materiaId, int cupo) {
        return new AsesoriaPeticion(materiaId, LocalDate.now().plusDays(2), LocalTime.of(11, 0), "Aula 9", cupo, null);
    }   
}