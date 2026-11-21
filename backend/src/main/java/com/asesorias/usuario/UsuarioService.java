package com.asesorias.usuario;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<UsuarioAdminResponse> listar(NombreRol rol, String q, Pageable pageable) {
        Page<Usuario> pagina = usuarios.findAll(UsuarioSpecs.filtrar(rol, q), pageable);
        return PaginaRespuesta.de(pagina.map(UsuarioAdminResponse::de));
    }
}