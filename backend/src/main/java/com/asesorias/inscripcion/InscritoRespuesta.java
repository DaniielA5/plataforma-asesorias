package com.asesorias.inscripcion;

import java.time.LocalDateTime;

public record InscritoRespuesta(
    Long id,
    Long alumnoId,
    String alumnoNombre ,
    String alumnoCorreo,
    String alumnoTelefono,
    LocalDateTime inscritoEn
){

    public static InscritoRespuesta de(Inscripcion i ) {
        return new InscritoRespuesta(
            i.getId(),
            i.getAlumno().getId(),
            i.getAlumno().getNombre(),
            i.getAlumno().getCorreo(),
            i.getAlumno().getTelefono(),
            i.getInscritoEn()
        );
    }

    }


