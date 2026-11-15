package com.asesorias.auth;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ValidadorTokenRevocado implements OAuth2TokenValidator<Jwt> {

    private final TokenRevocadoRepository tokensRevocados;

    public ValidadorTokenRevocado(TokenRevocadoRepository tokensRevocados) {
        this.tokensRevocados = tokensRevocados;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (tokensRevocados.existsByJti(jwt.getId())) {
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "El token fue revocado", null));
        }
        return OAuth2TokenValidatorResult.success();
    }
}