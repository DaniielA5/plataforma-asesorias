package com.asesorias.auth;

import com.asesorias.usuario.UsuarioRespuesta;

import java.time.Instant;

public record TokenRespuesta(
        String token,
        String tipo,
        Instant expiraEn,
        UsuarioRespuesta usuario
) {
}