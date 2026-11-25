package com.asesorias.auth;

import com.asesorias.error.ReglaNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component 
public class VerificadorGoogle {
        private static final String LLAVES_GOOGLE = "https://www.googleapis.com/oauth2/v3/certs";
    private static final List<String> EMISORES = List.of("https://accounts.google.com", "accounts.google.com");

    private final String clientId;
    private final NimbusJwtDecoder decoder;

    public VerificadorGoogle(@Value("${app.google.client-id:}") String clientId) {
        this.clientId = clientId;
        // No es un @Bean a propósito: ya existe un JwtDecoder (el de nuestros tokens) y chocarían.
        this.decoder = NimbusJwtDecoder.withJwkSetUri(LLAVES_GOOGLE).build();
        this.decoder.setJwtValidator(reglas(clientId));
    }

    /** Además de la firma y la caducidad: quién lo emitió, para quién y con qué correo. */
    static OAuth2TokenValidator<Jwt> reglas(String clientId) {
        return JwtValidators.createDefaultWithValidators(
                new JwtClaimValidator<Object>(JwtClaimNames.ISS,
                        emisor -> emisor != null && EMISORES.contains(emisor.toString())),
                new JwtClaimValidator<List<String>>(JwtClaimNames.AUD,
                        audiencia -> audiencia != null && audiencia.contains(clientId)),
                new JwtClaimValidator<Boolean>("email_verified", Boolean.TRUE::equals),
                new JwtClaimValidator<String>("email", correo -> correo != null && !correo.isBlank()));
    }

    public CuentaGoogle verificar(String credencial) {
        if (clientId.isBlank()) {
            throw new ReglaNegocioException("El inicio de sesión con Google no está configurado");
        }

        Jwt token;
        try {
            token = decoder.decode(credencial);
        } catch (JwtException e) {
            throw new ReglaNegocioException("No pudimos validar tu cuenta de Google, intenta de nuevo");
        }

        String correo = token.getClaimAsString("email").trim().toLowerCase();
        // Google solo manda en el correo si es Gmail o de Google Workspace (claim "hd").
        boolean correoConfiable = correo.endsWith("@gmail.com") || token.hasClaim("hd");
        return new CuentaGoogle(token.getSubject(), correo, token.getClaimAsString("name"), correoConfiable);
    } 
}
