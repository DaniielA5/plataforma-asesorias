package com.asesorias.usuario;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PaginaRespuesta<UsuarioAdminResponse> listar(
            @RequestParam(required = false) NombreRol rol,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return usuarios.listar(rol, q, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioAdminResponse crearProfesor(@Valid @RequestBody ProfesorRequest peticion) {
        return usuarios.crearProfesor(peticion);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioAdminResponse editar(@PathVariable Long id, @Valid @RequestBody EdicionUsuarioRequest peticion) {
        return usuarios.editar(id, peticion);
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioAdminResponse desactivar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return usuarios.cambiarEstado(id, false, Long.valueOf(jwt.getSubject()));
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioAdminResponse activar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return usuarios.cambiarEstado(id, true, Long.valueOf(jwt.getSubject()));
    }
}