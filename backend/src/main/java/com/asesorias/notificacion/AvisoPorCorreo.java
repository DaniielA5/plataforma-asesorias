package com.asesorias.notificacion;

import com.asesorias.correo.CorreoService;
import com.asesorias.correo.PlantillasCorreo;
import com.asesorias.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Manda el aviso por correo y lo registra en notificaciones, igual que el SMS y el WhatsApp. Nunca lanza excepción. */
@Service
public class AvisoPorCorreo {

    private static final Logger log = LoggerFactory.getLogger(AvisoPorCorreo.class);

    private final CorreoService correo;
    private final NotificacionRepository notificaciones;

    public AvisoPorCorreo(CorreoService correo, NotificacionRepository notificaciones) {
        this.correo = correo;
        this.notificaciones = notificaciones;
    }

    public void avisar(Usuario destinatario, String asunto, String mensaje) {
        if (destinatario.getCorreo() == null || destinatario.getCorreo().isBlank()) {
            return;
        }
        PlantillasCorreo.Contenido contenido = PlantillasCorreo.aviso(asunto, mensaje);
        boolean enviado = correo.enviar(destinatario.getCorreo(), asunto, contenido.html(), contenido.texto());

        Notificacion registro = new Notificacion();
        registro.setUsuarioId(destinatario.getId());
        registro.setCanal("EMAIL");
        registro.setDestino(recortar(destinatario.getCorreo(), 120));
        registro.setAsunto(recortar(mensaje, 200));
        registro.setEstado(enviado ? "ENVIADA" : "FALLIDA");
        if (!enviado) {
            registro.setError("No se pudo entregar el correo al servidor de correo (revisa el log)");
        }

        try {
            notificaciones.save(registro);
        } catch (Exception e) {
            log.error("No se pudo guardar la notificación: {}", e.getMessage());
        }
    }

    private String recortar(String texto, int maximo) {
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }
}