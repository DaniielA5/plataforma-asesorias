package com.asesorias.notificacion;

import java.time.LocalDateTime;

public record NotificacionResponse(
        Long id,
        Long usuarioId,
        String canal,
        String destino,
        String asunto,
        String estado,
        String error,
        LocalDateTime enviadaEn
) {

    public static NotificacionResponse de(Notificacion n) {
        return new NotificacionResponse(n.getId(), n.getUsuarioId(), n.getCanal(), n.getDestino(),
                n.getAsunto(), n.getEstado(), n.getError(), n.getEnviadaEn());
    }
}