package com.asesorias.asesoria;

import com.asesorias.comun.PaginaRespuesta;
import com.asesorias.error.RecursoNoEncontradoException;
import com.asesorias.error.ReglaNegocioException;
import com.asesorias.inscripcion.InscripcionRepository;
import com.asesorias.materia.Materia;
import com.asesorias.materia.MateriaRepository;
import com.asesorias.usuario.Usuario;
import com.asesorias.usuario.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asesorias.inscripcion.Inscripcion;
import com.asesorias.notificacion.AvisoPorCorreo;
import com.asesorias.notificacion.NotificacionService;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AsesoriaService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AsesoriaRepository asesorias;
    private final InscripcionRepository inscripciones;
    private final MateriaRepository materias;
    private final UsuarioRepository usuarios;
    private final NotificacionService notificaciones;
    private final AvisoPorCorreo avisoCorreo;

    public AsesoriaService(AsesoriaRepository asesorias, InscripcionRepository inscripciones,
                            MateriaRepository materias, UsuarioRepository usuarios,
                            NotificacionService notificaciones, AvisoPorCorreo avisoCorreo) {
        this.asesorias = asesorias;
        this.inscripciones = inscripciones;
        this.materias = materias;
        this.usuarios = usuarios;
        this.notificaciones = notificaciones;
        this.avisoCorreo = avisoCorreo;
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<AsesoriaRespuesta> listar(Long materiaId, EstadoAsesoria estado, Pageable pageable) {
        Specification<Asesoria> spec = Specification
                .where(conMateria(materiaId))
                .and(conEstado(estado));

        Page<Asesoria> pagina = asesorias.findAll(spec, pageable);
        Map<Long, Long> conteos = contarInscritos(pagina.getContent());

        return PaginaRespuesta.de(pagina.map(a -> AsesoriaRespuesta.de(a, conteos.getOrDefault(a.getId(), 0L))));
    }

    private static Specification<Asesoria> conMateria(Long materiaId) {
        return (root, query, cb) ->
                materiaId == null ? null : cb.equal(root.get("materia").get("id"), materiaId);
    }

    private static Specification<Asesoria> conEstado(EstadoAsesoria estado) {
        return (root, query, cb) ->
                estado == null ? null : cb.equal(root.get("estado"), estado);
    }

    
    @Transactional(readOnly = true)
    public PaginaRespuesta<AsesoriaRespuesta> listarDelProfesor(Long profesorId, Pageable pageable) {
        Page<Asesoria> pagina = asesorias.findByProfesorId(profesorId, pageable);
        Map<Long, Long> conteos = contarInscritos(pagina.getContent());

        return PaginaRespuesta.de(pagina.map(a -> AsesoriaRespuesta.de(a, conteos.getOrDefault(a.getId(), 0L))));
    }

    @Transactional(readOnly = true)
    public AsesoriaRespuesta ver(Long id) {
        Asesoria asesoria = buscar(id);
        return AsesoriaRespuesta.de(asesoria, inscripciones.countByAsesoriaId(id));
    }

    @Transactional
    public AsesoriaRespuesta crear(Long profesorId, AsesoriaPeticion peticion) {
        Materia materia = materias.findById(peticion.materiaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La materia no existe"));
        exigirQueLaImparte(profesorId, materia);
        Usuario profesor = usuarios.getReferenceById(profesorId);

        Asesoria asesoria = new Asesoria();
        asesoria.setProfesor(profesor);
        aplicarCambios(asesoria, materia, peticion);
        asesorias.save(asesoria);

        return AsesoriaRespuesta.de(asesoria, 0);
    }

    @Transactional
    public AsesoriaRespuesta editar(Long profesorId, Long id, AsesoriaPeticion peticion) {
        Asesoria asesoria = buscarPropia(profesorId, id);
        if (asesoria.getEstado() != EstadoAsesoria.ACTIVA) {
            throw new ReglaNegocioException("Solo se pueden editar asesorías activas");
        }
        Materia materia = materias.findById(peticion.materiaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La materia no existe"));
        if (!materia.getId().equals(asesoria.getMateria().getId())) {
            exigirQueLaImparte(profesorId, materia);
        }
        long inscritos = inscripciones.countByAsesoriaId(id);
        if (peticion.cupoMax() < inscritos) {
            throw new ReglaNegocioException("El cupo no puede ser menor a los " + inscritos + " alumnos ya inscritos");
        }

        aplicarCambios(asesoria, materia, peticion);

        return AsesoriaRespuesta.de(asesoria, inscritos);
    }

    @Transactional
    public AsesoriaRespuesta cancelar(Long profesorId, Long id) {
        Asesoria asesoria = buscarPropia(profesorId, id);
        if (asesoria.getEstado() == EstadoAsesoria.CANCELADA) {
            throw new ReglaNegocioException("Esta asesoría ya está cancelada");
        }
        asesoria.setEstado(EstadoAsesoria.CANCELADA);

        List<Inscripcion> inscritos = inscripciones.findByAsesoriaId(id);
        String mensaje = mensajeCancelacionAsesoria(asesoria);
        for (Inscripcion i : inscritos) {
            notificaciones.enviarSms(i.getAlumno(), mensaje);
            avisoCorreo.avisar(i.getAlumno(), "Se canceló tu asesoría", mensaje);
        }

        return AsesoriaRespuesta.de(asesoria, inscritos.size());
    }

    private String mensajeCancelacionAsesoria(Asesoria asesoria) {
        return "El profesor canceló la asesoría de " + asesoria.getMateria().getNombre()
                + " del " + asesoria.getFecha().format(FECHA) + ".";
    }

    private void aplicarCambios(Asesoria asesoria, Materia materia, AsesoriaPeticion peticion) {
        asesoria.setMateria(materia);
        asesoria.setFecha(peticion.fecha());
        asesoria.setHora(peticion.hora());
        asesoria.setLugar(peticion.lugar());
        asesoria.setCupoMax(peticion.cupoMax());
        asesoria.setNotas(peticion.notas());
    }

    private void exigirQueLaImparte(Long profesorId, Materia materia) {
        if (!usuarios.existsByIdAndMateriasImpartidasId(profesorId, materia.getId())) {
            throw new ReglaNegocioException("La materia " + materia.getNombre() + " no está entre las que impartes");
        }
    }
    
    private Asesoria buscar(Long id) {
        return asesorias.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La asesoría no existe"));
    }

    private Asesoria buscarPropia(Long profesorId, Long id) {
        Asesoria asesoria = buscar(id);
        if (!asesoria.getProfesor().getId().equals(profesorId)) {
            throw new AccessDeniedException("No puedes modificar una asesoría que no es tuya");
        }
        return asesoria;
    }

    private Map<Long, Long> contarInscritos(List<Asesoria> lista) {
        if (lista.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = lista.stream().map(Asesoria::getId).toList();
        return inscripciones.contarPorAsesorias(ids).stream()
                .collect(Collectors.toMap(
                        InscripcionRepository.ConteoInscritos::getAsesoriaId,
                        InscripcionRepository.ConteoInscritos::getTotal));
    }
}