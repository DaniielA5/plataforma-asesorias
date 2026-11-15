package com.asesorias.asesoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsesoriaRepository extends JpaRepository<Asesoria, Long> {

    @Override
    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findByProfesorId(Long profesorId, Pageable pageable);
}