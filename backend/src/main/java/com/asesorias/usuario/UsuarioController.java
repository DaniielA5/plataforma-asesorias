package com.asesorias.usuario;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}