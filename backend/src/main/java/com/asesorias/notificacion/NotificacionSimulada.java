package com.asesorias.notificacion;

import com.asesorias.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.twilio.habilitado", havingValue = "false", matchIfMissing = true)
public class NotificacionSimulada implements NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionSimulada.class);

    @Override
    public void enviarSms(Usuario destinatario, String mensaje) {
        log.info("[SMS simulado] para {}: {}", destinatario.getTelefono(), mensaje);
    }

    @Override
    public void enviarWhatsapp(Usuario destinatario, String mensaje) {
        log.info("[WhatsApp simulado] para {}: {}", destinatario.getTelefono(), mensaje);
    }
}