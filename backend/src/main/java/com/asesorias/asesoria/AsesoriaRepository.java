package com.asesorias.asesoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
public interface AsesoriaRepository extends JpaRepository<Asesoria, Long>, JpaSpecificationExecutor<Asesoria> {

    @Override
    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findAll(Specification<Asesoria> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"profesor", "materia"})
    Page<Asesoria> findByProfesorId(Long profesorId, Pageable pageable);

    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asesoria a WHERE a.id = :id")
    java.util.Optional<Asesoria> buscarParaActualizar(@Param("id") Long id);
}