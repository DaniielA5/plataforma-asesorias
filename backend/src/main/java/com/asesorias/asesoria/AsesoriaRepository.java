package com.asesorias.asesoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AsesoriaRepository extends JpaRepository<Asesoria, Long>, JpaSpecificationExecutor<Asesoria> {

    @Override
    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findAll(Specification<Asesoria> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findByProfesorId(Long profesorId, Pageable pageable);
}