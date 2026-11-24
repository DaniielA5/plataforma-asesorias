package com.asesorias.notificacion;

import com.asesorias.correo.CorreoService;
import com.asesorias.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvisoPorCorreoTest {

    @Mock CorreoService correo;
    @Mock NotificacionRepository notificaciones;
    @InjectMocks AvisoPorCorreo aviso;

    @Test
    void siElCorreoSaleLoRegistraComoEnviada() {
        when(correo.enviar(anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        Notificacion guardada = avisarYCapturar();

        assertEquals("EMAIL", guardada.getCanal());
        assertEquals("ENVIADA", guardada.getEstado());
    }

    @Test
    void siElCorreoNoSaleLoRegistraComoFallida() {
        when(correo.enviar(anyString(), anyString(), anyString(), anyString())).thenReturn(false);

        Notificacion guardada = avisarYCapturar();

        assertEquals("FALLIDA", guardada.getEstado());
    }

    private Notificacion avisarYCapturar() {
        Usuario alumno = new Usuario();
        alumno.setId(7L);
        alumno.setCorreo("alumno@example.com");

        aviso.avisar(alumno, "Asunto", "Mensaje");

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificaciones).save(captor.capture());
        return captor.getValue();
    }
}