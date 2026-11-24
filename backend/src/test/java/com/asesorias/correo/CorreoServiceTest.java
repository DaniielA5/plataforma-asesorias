package com.asesorias.correo;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CorreoServiceTest {

    @Test
    void elRemitenteSeMuestraComoAyudITOYNoComoNoReply() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        when(sender.createMimeMessage()).thenReturn(new JavaMailSenderImpl().createMimeMessage());
        CorreoService servicio = new CorreoService(sender, "no-reply@mg.juarez.top");

        servicio.enviar("alumno@asesorias.com", "Asunto", "<p>Hola</p>", "Hola");

        ArgumentCaptor<MimeMessage> enviado = ArgumentCaptor.forClass(MimeMessage.class);
        verify(sender).send(enviado.capture());
        assertEquals("AyudITO <no-reply@mg.juarez.top>", enviado.getValue().getFrom()[0].toString());
    }
}