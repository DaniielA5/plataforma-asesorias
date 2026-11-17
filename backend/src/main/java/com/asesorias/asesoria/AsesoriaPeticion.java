package com.asesorias.asesoria;
 
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
public record AsesoriaPeticion(
    @NotNull Long materiaId,
    @NotNull @FutureOrPresent LocalDate fecha,
    @NotNull LocalTime hora,
    @NotBlank @Size (max = 120) String lugar , 
    @NotNull @Min(1) @Max(100) Integer cupoMax,
    @Size(max = 500) String notas
) {
}
