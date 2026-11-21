package com.asesorias.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findByCorreo(String correo);

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findConRolById(Long id);

    boolean existsByCorreo(String correo);

    @Override
    @EntityGraph(attributePaths = "rol")
    Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);
}