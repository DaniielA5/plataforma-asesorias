package com.asesorias.auth;

import com.asesorias.usuario.UsuarioRespuesta;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenRespuesta registrar(@Valid @RequestBody RegistroPeticion peticion) {
        return authService.registrar(peticion);
    }

    @PostMapping("/login")
    public TokenRespuesta login(@Valid @RequestBody LoginPeticion peticion) {
        return authService.login(peticion);
    }

    @GetMapping("/me")
    public UsuarioRespuesta yo(@AuthenticationPrincipal Jwt jwt) {
        return authService.usuarioActual(Long.valueOf(jwt.getSubject()));
    }
}