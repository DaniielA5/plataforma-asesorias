package com.asesorias.notificacion;

import com.asesorias.usuario.Usuario;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.twilio.habilitado", havingValue = "true")
public class NotificacionTwilio implements NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionTwilio.class);
    private static final int MAX_ERROR = 500;

    private final TwilioProperties propiedades;
    private final NotificacionRepository notificaciones;

    public NotificacionTwilio(TwilioProperties propiedades, NotificacionRepository notificaciones) {
        this.propiedades = propiedades;
        this.notificaciones = notificaciones;
    }

    @PostConstruct
    void iniciar() {
        Twilio.init(propiedades.accountSid(), propiedades.authToken());
    }

    @Override
    public void enviarSms(Usuario destinatario, String mensaje) {
        String cuerpo = (propiedades.plantillaSms() == null || propiedades.plantillaSms().isBlank())
                ? mensaje
                : propiedades.plantillaSms();
        enviar("SMS", destinatario, mensaje, cuerpo,
                destinatario.getTelefono(), propiedades.numeroSms());
    }

    @Override
    public void enviarWhatsapp(Usuario destinatario, String mensaje) {
        enviar("WHATSAPP", destinatario, mensaje, mensaje,
                "whatsapp:" + destinatario.getTelefono(), "whatsapp:" + propiedades.numeroWhatsapp());
    }

    private void enviar(String canal, Usuario destinatario, String mensaje, String cuerpo, String para, String de) {
        Notificacion registro = new Notificacion();
        registro.setUsuarioId(destinatario.getId());
        registro.setCanal(canal);
        registro.setDestino(recortar(para, 120));
        registro.setAsunto(recortar(mensaje, 200));

        try {
            if (destinatario.getTelefono() == null || destinatario.getTelefono().isBlank()) {
                throw new IllegalStateException("El usuario no tiene teléfono registrado");
            }
            crearMensaje(canal, cuerpo, para, de).create();
            registro.setEstado("ENVIADA");
        } catch (Exception e) {
            log.warn("No se pudo enviar el {} a {}: {}", canal, para, e.getMessage());
            registro.setEstado("FALLIDA");
            registro.setError(recortar(e.getMessage(), MAX_ERROR));
        }

        try {
            notificaciones.save(registro);
        } catch (Exception e) {
            log.error("No se pudo guardar la notificación: {}", e.getMessage());
        }
    }

    private MessageCreator crearMensaje(String canal, String cuerpo, String para, String de) {
        PhoneNumber destino = new PhoneNumber(para);
        PhoneNumber origen = new PhoneNumber(de);

        boolean conPlantilla = "WHATSAPP".equals(canal)
                && propiedades.contentSidWhatsapp() != null
                && !propiedades.contentSidWhatsapp().isBlank();
        if (!conPlantilla) {
            return Message.creator(destino, origen, cuerpo);
        }

        // Cuenta de prueba: WhatsApp solo admite to, from y ContentSid (sin Body ni variables).
        return Message.creator(destino, origen, (String) null)
                .setContentSid(propiedades.contentSidWhatsapp());
    }

    private String recortar(String texto, int maximo) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }
}