package com.asesorias.usuario;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findByCorreo(String correo);

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findConRolById(Long id);

    boolean existsByCorreo(String correo);
}