package com.asesorias.notificacion;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionConsultaService notificaciones;

    public NotificacionController(NotificacionConsultaService notificaciones) {
        this.notificaciones = notificaciones;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PaginaRespuesta<NotificacionResponse> listar(
            @RequestParam(required = false) String canal,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 10, sort = "enviadaEn", direction = Sort.Direction.DESC) Pageable pageable) {
        return notificaciones.listar(canal, estado, pageable);
    }
}