package com.asesorias.inscripcion;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByAsesoriaIdAndAlumnoId(Long asesoriaId, Long alumnoId);

    long countByAsesoriaId(Long asesoriaId);

    @EntityGraph(attributePaths = "alumno")
    List<Inscripcion> findByAsesoriaId(Long asesoriaId);

    @EntityGraph(attributePaths = {"asesoria", "asesoria.materia", "asesoria.profesor"})
    List<Inscripcion> findByAlumnoId(Long alumnoId);

    @Query ("SELECT i.asesoria.id AS asesoriaId, COUNT(i) AS total " + 
        "FROM Inscripcion i WHERE i.asesoria.id IN :ids GROUP BY i.asesoria.id")
    List<ConteoInscritos> contarPorAsesorias(@Param("ids") List<Long> ids);

    interface ConteoInscritos {
        Long getAsesoriaId();
        Long getTotal();
    }
}