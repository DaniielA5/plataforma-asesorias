package com.asesorias.materia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MateriaRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20, message = "El código no puede pasar de 20 caracteres")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre no puede pasar de 120 caracteres")
        String nombre
) {
}