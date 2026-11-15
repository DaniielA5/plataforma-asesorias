package com.asesorias.inscripcion;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByAsesoriaIdAndAlumnoId(Long asesoriaId, Long alumnoId);

    long countByAsesoriaId(Long asesoriaId);

    @EntityGraph(attributePaths = "alumno")
    List<Inscripcion> findByAsesoriaId(Long asesoriaId);

    @EntityGraph(attributePaths = {"asesoria", "asesoria.materia", "asesoria.profesor"})
    List<Inscripcion> findByAlumnoId(Long alumnoId);
}