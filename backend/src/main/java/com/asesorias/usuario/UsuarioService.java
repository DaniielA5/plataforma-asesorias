package com.asesorias.usuario;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asesorias.error.ReglaNegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<UsuarioAdminResponse> listar(NombreRol rol, String q, Pageable pageable) {
        Page<Usuario> pagina = usuarios.findAll(UsuarioSpecs.filtrar(rol, q), pageable);
        return PaginaRespuesta.de(pagina.map(UsuarioAdminResponse::de));
    }

        @Transactional
    public UsuarioAdminResponse crearProfesor(ProfesorRequest peticion) {
        String correo = peticion.correo().trim().toLowerCase();
        if (usuarios.existsByCorreo(correo)) {
            throw new ReglaNegocioException("Ese correo ya está registrado");
        }

        Usuario profesor = new Usuario();
        profesor.setNombre(peticion.nombre().trim());
        profesor.setCorreo(correo);
        profesor.setPasswordHash(passwordEncoder.encode(peticion.password()));
        profesor.setTelefono(peticion.telefono());
        profesor.setRol(roles.findByNombre(NombreRol.PROFESOR).orElseThrow());
        return UsuarioAdminResponse.de(usuarios.save(profesor));
    }
}