package com.asesorias.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificadorGoogleTest {

    private static final String CLIENTE = "mi-cliente.apps.googleusercontent.com";

    @Test
    void tokenCorrectoPasa() {
        assertTrue(pasa(token().build()));
    }

    @Test
    void emisorSinHttpsTambienPasa() {
        assertTrue(pasa(token().claim("iss", "accounts.google.com").build()));
    }

    @Test
    void tokenParaOtraAplicacionSeRechaza() {
        assertFalse(pasa(token().audience(List.of("otra-app")).build()));
    }

    @Test
    void emisorQueNoEsGoogleSeRechaza() {
        assertFalse(pasa(token().claim("iss", "https://impostor.example.com").build()));
    }

    @Test
    void correoSinVerificarSeRechaza() {
        assertFalse(pasa(token().claim("email_verified", false).build()));
    }

    @Test
    void tokenVencidoSeRechaza() {
        Instant haceDosHoras = Instant.now().minusSeconds(7200);
        assertFalse(pasa(token().issuedAt(haceDosHoras).expiresAt(haceDosHoras.plusSeconds(3600)).build()));
    }

    private boolean pasa(Jwt jwt) {
        return !VerificadorGoogle.reglas(CLIENTE).validate(jwt).hasErrors();
    }

    private Jwt.Builder token() {
        return Jwt.withTokenValue("prueba")
                .header("alg", "RS256")
                .subject("123")
                .audience(List.of(CLIENTE))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("iss", "https://accounts.google.com")
                .claim("email", "ana@gmail.com")
                .claim("email_verified", true);
    }
}
