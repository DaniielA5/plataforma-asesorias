package com.asesorias.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PerfilRequest(
        @NotBlank(message = "Escribe tu nombre")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String nombre,

        @NotBlank(message = "Escribe tu teléfono")
        @Pattern(regexp = "^\\+[1-9]\\d{9,14}$",
                message = "Escribe el teléfono con lada, por ejemplo +529512345672")
        String telefono
) {
}