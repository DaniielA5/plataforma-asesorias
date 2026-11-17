package com.asesorias.asesoria;

import java.time.LocalDate;
import java.time.LocalTime;

 
public record AsesoriaRespuesta(
    Long id,
    Long profesorId,
    String profesorNombre,
    Long materiaId,
    String materiaNombre,
    LocalDate fecha,
    LocalTime hora,
    String lugar ,
    Integer cupoMax,
    long inscritos,
    EstadoAsesoria estado, 
    String notas
) {

public static AsesoriaRespuesta de(Asesoria a, long inscritos) {
    return new AsesoriaRespuesta(
        a.getId(),
        a.getProfesor().getId(),
        a.getProfesor().getNombre(),
        a.getMateria().getId(),
        a.getMateria().getNombre(),
        a.getFecha(),
        a.getHora(),
        a.getLugar(),
        a.getCupoMax(),
        inscritos,
        a.getEstado(),
        a.getNotas()
    );
}

}