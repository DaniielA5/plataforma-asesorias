package com.asesorias.inscripcion;

import com.asesorias.asesoria.Asesoria;
import com.asesorias.asesoria.AsesoriaRepository;
import com.asesorias.asesoria.EstadoAsesoria;
import com.asesorias.error.RecursoNoEncontradoException;
import com.asesorias.error.ReglaNegocioException;
import com.asesorias.notificacion.NotificacionService;
import com.asesorias.usuario.Usuario;
import com.asesorias.usuario.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripciones;
    private final AsesoriaRepository asesorias;
    private final UsuarioRepository usuarios;
    private final NotificacionService notificaciones;

    public InscripcionService(InscripcionRepository inscripciones, AsesoriaRepository asesorias,
                               UsuarioRepository usuarios, NotificacionService notificaciones) {
        this.inscripciones = inscripciones;
        this.asesorias = asesorias;
        this.usuarios = usuarios;
        this.notificaciones = notificaciones;
    }

    @Transactional
    public InscritoRespuesta inscribirse(Long alumnoId, Long asesoriaId) {
        Asesoria asesoria = asesorias.buscarParaActualizar(asesoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La asesoría no existe"));

        if (asesoria.getEstado() != EstadoAsesoria.ACTIVA) {
            throw new ReglaNegocioException("Esta asesoría ya no está activa");
        }
        if (inscripciones.existsByAsesoriaIdAndAlumnoId(asesoriaId, alumnoId)) {
            throw new ReglaNegocioException("Ya estás inscrito en esta asesoría");
        }
        if (inscripciones.countByAsesoriaId(asesoriaId) >= asesoria.getCupoMax()) {
            throw new ReglaNegocioException("Ya no hay cupo disponible");
        }

        Usuario alumno = usuarios.findById(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario ya no existe"));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setAsesoria(asesoria);
        inscripcion.setAlumno(alumno);
        inscripciones.save(inscripcion);

        notificaciones.enviarWhatsapp(alumno, mensajeConfirmacion(asesoria));

        return InscritoRespuesta.de(inscripcion);
    }

    @Transactional
    public void cancelarInscripcion(Long alumnoId, Long asesoriaId) {
        Inscripcion inscripcion = inscripciones.findByAsesoriaIdAndAlumnoId(asesoriaId, alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No estás inscrito en esta asesoría"));

        Asesoria asesoria = inscripcion.getAsesoria();
        Usuario alumno = inscripcion.getAlumno();

        inscripciones.delete(inscripcion);

        notificaciones.enviarWhatsapp(alumno, mensajeCancelacion(asesoria));
    }

    @Transactional(readOnly = true)
    public List<InscritoRespuesta> listarInscritos(Long profesorId, Long asesoriaId) {
        Asesoria asesoria = asesorias.findById(asesoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La asesoría no existe"));
        if (!asesoria.getProfesor().getId().equals(profesorId)) {
            throw new AccessDeniedException("No puedes ver los inscritos de una asesoría que no es tuya");
        }
        return inscripciones.findByAsesoriaId(asesoriaId).stream()
                .map(InscritoRespuesta::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MiInscripcionRespuesta> listarMisInscripciones(Long alumnoId) {
        List<Inscripcion> lista = inscripciones.findByAlumnoId(alumnoId);
        Map<Long, Long> conteos = contarInscritos(lista.stream()
                .map(i -> i.getAsesoria().getId()).distinct().toList());

        return lista.stream()
                .map(i -> MiInscripcionRespuesta.de(i, conteos.getOrDefault(i.getAsesoria().getId(), 0L)))
                .toList();
    }

    private Map<Long, Long> contarInscritos(List<Long> asesoriaIds) {
        if (asesoriaIds.isEmpty()) {
            return Map.of();
        }
        return inscripciones.contarPorAsesorias(asesoriaIds).stream()
                .collect(Collectors.toMap(
                        InscripcionRepository.ConteoInscritos::getAsesoriaId,
                        InscripcionRepository.ConteoInscritos::getTotal));
    }

    private String mensajeConfirmacion(Asesoria asesoria) {
        return "Te inscribiste a " + asesoria.getMateria().getNombre() + " el " + asesoria.getFecha()
                + " a las " + asesoria.getHora() + " en " + asesoria.getLugar();
    }

    private String mensajeCancelacion(Asesoria asesoria) {
        return "Cancelaste tu inscripción a " + asesoria.getMateria().getNombre() + " del " + asesoria.getFecha();
    }
}