package com.asesorias.notificacion;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.twilio")
public record TwilioProperties(
        boolean habilitado,
        String accountSid,
        String authToken,
        String numeroSms,
        String numeroWhatsapp,
        String plantillaSms
) {
}