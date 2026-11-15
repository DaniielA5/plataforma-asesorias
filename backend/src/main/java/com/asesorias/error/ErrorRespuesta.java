package com.asesorias.error;

import java.time.Instant;
import  java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorRespuesta(
    Instant fecha,
    int estado,
    String error,
    String mensaje,
    String ruta,
    Map<String, String> errores) {
}
