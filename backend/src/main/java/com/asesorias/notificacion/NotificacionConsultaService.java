package com.asesorias.notificacion;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionConsultaService {

    private final NotificacionRepository notificaciones;

    public NotificacionConsultaService(NotificacionRepository notificaciones) {
        this.notificaciones = notificaciones;
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<NotificacionResponse> listar(String canal, String estado, Pageable pageable) {
        Page<Notificacion> pagina = notificaciones.findAll(NotificacionSpecs.filtrar(canal, estado), pageable);
        return PaginaRespuesta.de(pagina.map(NotificacionResponse::de));
    }
}