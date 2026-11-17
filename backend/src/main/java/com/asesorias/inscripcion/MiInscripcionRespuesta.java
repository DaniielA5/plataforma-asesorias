package com.asesorias.inscripcion;

import com.asesorias.asesoria.AsesoriaRespuesta;

import java.time.LocalDateTime;

public record MiInscripcionRespuesta(
        Long id,
        AsesoriaRespuesta asesoria,
        LocalDateTime inscritoEn
) {

    public static MiInscripcionRespuesta de(Inscripcion i, long inscritosDeEsaAsesoria) {
        return new MiInscripcionRespuesta(
                i.getId(),
                AsesoriaRespuesta.de(i.getAsesoria(), inscritosDeEsaAsesoria),
                i.getInscritoEn()
        );
    }
}