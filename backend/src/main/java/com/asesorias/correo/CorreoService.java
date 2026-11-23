package com.asesorias.correo;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    private static final Logger log = LoggerFactory.getLogger(CorreoService.class);

    private final JavaMailSender mailSender;
    private final String remitente;

    public CorreoService(JavaMailSender mailSender, @Value("${app.correo.remitente}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public void enviar(String destinatario, String asunto, String cuerpoHtml) {
        enviar(destinatario, asunto, cuerpoHtml, null);
    }

    /** Si hay texto plano, el correo lleva las dos versiones y el cliente de correo elige cuál mostrar. */
    public void enviar(String destinatario, String asunto, String cuerpoHtml, String cuerpoTexto) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            boolean conTexto = cuerpoTexto != null;
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, conTexto, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            if (conTexto) {
                helper.setText(cuerpoTexto, cuerpoHtml);
            } else {
                helper.setText(cuerpoHtml, true);
            }
            mailSender.send(mensaje);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo a {}: {}", destinatario, e.getMessage());
        }
    }
}