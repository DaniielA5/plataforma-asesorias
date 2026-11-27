package com.asesorias.notificacion;
import com.asesorias.usuario.Usuario;
public interface NotificacionService {

    //yo(daniel)  la voy a llmar al inscribir y al cancelar ; chay tu la implementas 
    //con twilio
    //importante es como la regla , nunca lanza excepciones , si el envio falla, se guarda como fallida en notificaciones 
    void enviarSms(Usuario destinatario, String mensaje);
    void enviarWhatsapp(Usuario destinatario, String mensaje);

    // Para proveedores que usan plantillas aprobadas (Meta). Por defecto se manda el texto de siempre,
    // así Twilio y la simulada siguen funcionando igual sin tocarlas.
    default void enviarWhatsappConPlantilla(Usuario destinatario, TipoAviso tipo,
                                            java.util.List<String> variables, String mensaje) {
    enviarWhatsapp(destinatario, mensaje);
    }
    
}

