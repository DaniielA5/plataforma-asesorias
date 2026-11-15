package com.asesorias.auth;

import com.asesorias.error.RecursoNoEncontradoException;
import com.asesorias.error.ReglaNegocioException;
import com.asesorias.usuario.NombreRol;
import com.asesorias.usuario.RolRepository;
import com.asesorias.usuario.Usuario;
import com.asesorias.usuario.UsuarioRepository;
import com.asesorias.usuario.UsuarioRespuesta;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRevocadoRepository tokensRevocados;

    public AuthService(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder passwordEncoder,
                       JwtService jwtService, TokenRevocadoRepository tokensRevocados) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokensRevocados = tokensRevocados;
    }

    @Transactional
    public TokenRespuesta registrar(RegistroPeticion peticion) {
        String correo = normalizar(peticion.correo());
        if (usuarios.existsByCorreo(correo)) {
            throw new ReglaNegocioException("Ese correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(peticion.nombre().trim());
        usuario.setCorreo(correo);
        usuario.setPasswordHash(passwordEncoder.encode(peticion.password()));
        usuario.setTelefono(peticion.telefono());
        usuario.setRol(roles.findByNombre(NombreRol.ALUMNO).orElseThrow());
        usuarios.save(usuario);

        return responderConToken(usuario);
    }

    @Transactional(readOnly = true)
    public TokenRespuesta login(LoginPeticion peticion) {
        Usuario usuario = usuarios.findByCorreo(normalizar(peticion.correo()))
                .filter(u -> passwordEncoder.matches(peticion.password(), u.getPasswordHash()))
                .orElseThrow(() -> new BadCredentialsException("Correo o contraseña incorrectos"));
        if (!usuario.isActivo()) {
            throw new DisabledException("Usuario desactivado");
        }
        return responderConToken(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioRespuesta usuarioActual(Long id) {
        return usuarios.findConRolById(id)
                .map(UsuarioRespuesta::de)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario ya no existe"));
    }
    @Transactional
    public void logout(Jwt jwt) {
        TokenRevocado revocado = new TokenRevocado();
        revocado.setJti(jwt.getId());
        revocado.setExpiraEn(LocalDateTime.ofInstant(jwt.getExpiresAt(), ZoneId.systemDefault()));
        tokensRevocados.save(revocado);
    }

    private TokenRespuesta responderConToken(Usuario usuario) {
        Jwt jwt = jwtService.emitir(usuario);
        return new TokenRespuesta(jwt.getTokenValue(), "Bearer", jwt.getExpiresAt(), UsuarioRespuesta.de(usuario));
    }

    private String normalizar(String correo) {
        return correo.trim().toLowerCase();
    }
}