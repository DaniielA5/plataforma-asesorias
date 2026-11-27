package com.asesorias.notificacion;

import com.asesorias.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Primary
@ConditionalOnProperty(name = "app.meta.habilitado", havingValue = "true")
public class NotificacionMeta implements NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionMeta.class);
    private static final int MAX_ERROR = 500;

    private final MetaProperties propiedades;
    private final NotificacionRepository notificaciones;
    private final RestClient http;

    public NotificacionMeta(MetaProperties propiedades, NotificacionRepository notificaciones) {
        this.propiedades = propiedades;
        this.notificaciones = notificaciones;
        this.http = RestClient.builder().baseUrl("https://graph.facebook.com").build();
    }

    @Override
    public void enviarSms(Usuario destinatario, String mensaje) {
        log.info("[SMS simulado, Meta no manda SMS] para {}: {}", destinatario.getTelefono(), mensaje);
    }

    @Override
    public void enviarWhatsapp(Usuario destinatario, String mensaje) {
        enviar(destinatario, mensaje, null, List.of());
    }

    @Override
    public void enviarWhatsappConPlantilla(Usuario destinatario, TipoAviso tipo,
                                           List<String> variables, String mensaje) {
        String plantilla = switch (tipo) {
            case CONFIRMACION_INSCRIPCION -> propiedades.plantillaConfirmacion();
            case CANCELACION_INSCRIPCION -> propiedades.plantillaCancelacionInscripcion();
            case CANCELACION_ASESORIA -> propiedades.plantillaCancelacionAsesoria();
        };
        enviar(destinatario, mensaje, plantilla, variables);
    }

    private void enviar(Usuario destinatario, String mensaje, String plantilla, List<String> variables) {
        String para = soloDigitos(destinatario.getTelefono());

        Notificacion registro = new Notificacion();
        registro.setUsuarioId(destinatario.getId());
        registro.setCanal("WHATSAPP");
        registro.setDestino(recortar(para, 120));
        registro.setAsunto(recortar(mensaje, 200));

        try {
            if (para.isBlank()) {
                throw new IllegalStateException("El usuario no tiene teléfono registrado");
            }
            Map<String, Object> cuerpo = (plantilla == null || plantilla.isBlank())
                    ? cuerpoTexto(para, mensaje)
                    : cuerpoPlantilla(para, plantilla, variables);

            http.post()
                    .uri("/{version}/{id}/messages", propiedades.versionOrDefault(), propiedades.phoneNumberId())
                    .header("Authorization", "Bearer " + propiedades.accessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .retrieve()
                    .toBodilessEntity();
            registro.setEstado("ENVIADA");
        } catch (Exception e) {
            log.warn("No se pudo enviar el WhatsApp a {}: {}", para, e.getMessage());
            registro.setEstado("FALLIDA");
            registro.setError(recortar(e.getMessage(), MAX_ERROR));
        }

        try {
            notificaciones.save(registro);
        } catch (Exception e) {
            log.error("No se pudo guardar la notificación: {}", e.getMessage());
        }
    }

    private Map<String, Object> cuerpoTexto(String para, String mensaje) {
        return Map.of(
                "messaging_product", "whatsapp",
                "to", para,
                "type", "text",
                "text", Map.of("body", mensaje));
    }

    private Map<String, Object> cuerpoPlantilla(String para, String plantilla, List<String> variables) {
        List<Map<String, String>> parametros = new ArrayList<>();
        for (String valor : variables) {
            parametros.add(Map.of("type", "text", "text", limpiar(valor)));
        }
        Map<String, Object> plantillaJson = Map.of(
                "name", plantilla,
                "language", Map.of("code", propiedades.idiomaOrDefault()),
                "components", List.of(Map.of("type", "body", "parameters", parametros)));
        return Map.of(
                "messaging_product", "whatsapp",
                "to", para,
                "type", "template",
                "template", plantillaJson);
    }

    // Meta no acepta saltos de línea, tabuladores ni muchos espacios seguidos dentro de una variable.
    private String limpiar(String valor) {
        if (valor == null || valor.isBlank()) {
            return "-";
        }
        return valor.replaceAll("[\\r\\n\\t]+", " ").replaceAll(" {2,}", " ").trim();
    }

    // Meta pide solo dígitos con lada de país. Si vienen 10 dígitos (México), se le antepone 52.
    private String soloDigitos(String telefono) {
        if (telefono == null) {
            return "";
        }
        String digitos = telefono.replaceAll("\\D", "");
        return digitos.length() == 10 ? "52" + digitos : digitos;
    }

    private String recortar(String texto, int maximo) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }
}