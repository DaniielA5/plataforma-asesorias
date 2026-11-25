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
import com.asesorias.correo.CorreoService;
import org.springframework.beans.factory.annotation.Value;
import com.asesorias.correo.PlantillasCorreo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRevocadoRepository tokensRevocados;
    private final TokenRecuperacionRepository tokensRecuperacion;
    private final CorreoService correo;
    private final VerificadorGoogle verificadorGoogle;
    private final String urlFrontend;

    public AuthService(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder passwordEncoder,
                       JwtService jwtService, TokenRevocadoRepository tokensRevocados,
                       TokenRecuperacionRepository tokensRecuperacion, CorreoService correo,
                       VerificadorGoogle verificadorGoogle,
                       @Value("${app.frontend.url}") String urlFrontend) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokensRevocados = tokensRevocados;
        this.tokensRecuperacion = tokensRecuperacion;
        this.correo = correo;
        this.verificadorGoogle = verificadorGoogle;
        this.urlFrontend = urlFrontend;
    }


    @Transactional
    public TokenRespuesta registrar(RegistroPeticion peticion) {
        String correoNormalizado = normalizar(peticion.correo());
        if (usuarios.existsByCorreo(correoNormalizado)) {
            throw new ReglaNegocioException("Ese correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(peticion.nombre().trim());
        usuario.setCorreo(correoNormalizado);
        usuario.setPasswordHash(passwordEncoder.encode(peticion.password()));
        usuario.setTelefono(peticion.telefono());
        usuario.setRol(roles.findByNombre(NombreRol.ALUMNO).orElseThrow());
        usuarios.save(usuario);

        enviarBienvenida(usuario);
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

    @Transactional
    public TokenRespuesta loginConGoogle(GooglePeticion peticion) {
        CuentaGoogle cuenta = verificadorGoogle.verificar(peticion.credencial());
        Usuario usuario = usuarios.findByGoogleSub(cuenta.sub())
                .orElseGet(() -> vincularOCrear(cuenta));
        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("Tu cuenta está desactivada, habla con el administrador");
        }
        return responderConToken(usuario);
    }

    private Usuario vincularOCrear(CuentaGoogle cuenta) {
        return usuarios.findByCorreo(cuenta.correo())
                .map(existente -> vincular(existente, cuenta))
                .orElseGet(() -> crearDesdeGoogle(cuenta));
    }

    private Usuario vincular(Usuario usuario, CuentaGoogle cuenta) {
        if (!cuenta.correoConfiable()) {
            throw new ReglaNegocioException("Ese correo ya tiene cuenta, entra con tu contraseña");
        }
        if (usuario.getGoogleSub() != null) {
            throw new ReglaNegocioException("Ese correo ya está unido a otra cuenta de Google");
        }
        usuario.setGoogleSub(cuenta.sub());
        // El registro no comprueba el correo: la contraseña anterior pudo ponerla otra persona.
        usuario.setPasswordHash(passwordAleatorio());
        return usuario;
    }

    private Usuario crearDesdeGoogle(CuentaGoogle cuenta) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombreDe(cuenta));
        usuario.setCorreo(cuenta.correo());
        usuario.setGoogleSub(cuenta.sub());
        usuario.setPasswordHash(passwordAleatorio());
        usuario.setRol(roles.findByNombre(NombreRol.ALUMNO).orElseThrow());
        usuarios.save(usuario);
        enviarBienvenida(usuario);
        return usuario;
    }

    private String nombreDe(CuentaGoogle cuenta) {
        String nombre = cuenta.nombre() == null || cuenta.nombre().isBlank()
                ? cuenta.correo().split("@")[0]
                : cuenta.nombre().trim();
        return nombre.length() <= 120 ? nombre : nombre.substring(0, 120);
    }

    // La columna es obligatoria. Nadie conoce esta contraseña; se puede crear una con "recuperar".
    private String passwordAleatorio() {
        return passwordEncoder.encode(generarTokenCrudo());
    }

    private void enviarBienvenida(Usuario usuario) {
        PlantillasCorreo.Contenido bienvenida =
                PlantillasCorreo.bienvenida(usuario.getNombre(), usuario.getCorreo(), urlFrontend);
        correo.enviar(usuario.getCorreo(), "¡Bienvenido a AyudITO!", bienvenida.html(), bienvenida.texto());
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

    @Transactional
    public void recuperar(RecuperacionPeticion peticion) {        usuarios.findByCorreo(normalizar(peticion.correo())).ifPresent(usuario -> {
            String tokenCrudo = generarTokenCrudo();

            TokenRecuperacion token = new TokenRecuperacion();
            token.setUsuario(usuario);
            token.setTokenHash(hash(tokenCrudo));
            token.setExpiraEn(LocalDateTime.now().plusMinutes(30));
            tokensRecuperacion.save(token);

            String enlace = urlFrontend + "/restablecer?token=" + tokenCrudo;
                        PlantillasCorreo.Contenido recuperacion = PlantillasCorreo.recuperacion(enlace);
            correo.enviar(usuario.getCorreo(), "Recupera tu contraseña", recuperacion.html(), recuperacion.texto());
        });
    }

    @Transactional
    public void restablecer(RestablecerPeticion peticion) {
        TokenRecuperacion token = tokensRecuperacion.findByTokenHash(hash(peticion.token()))
                .filter(t -> !t.isUsado())
                .filter(t -> t.getExpiraEn().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new ReglaNegocioException("El enlace ya no es válido, pide uno nuevo"));

        Usuario usuario = token.getUsuario();
        usuario.setPasswordHash(passwordEncoder.encode(peticion.nuevaPassword()));
        token.setUsado(true);
    }

    private String generarTokenCrudo() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}