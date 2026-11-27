package com.asesorias.notificacion;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.meta")
public record MetaProperties(
        boolean habilitado,
        String phoneNumberId,
        String accessToken,
        String version,
        String idioma,
        String plantillaConfirmacion,
        String plantillaCancelacionInscripcion,
        String plantillaCancelacionAsesoria
) {

    public String versionOrDefault() {
        return (version == null || version.isBlank()) ? "v25.0" : version;
    }

    public String idiomaOrDefault() {
        return (idioma == null || idioma.isBlank()) ? "es_MX" : idioma;
    }
}