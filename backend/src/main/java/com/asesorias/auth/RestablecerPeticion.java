package com.asesorias.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RestablecerPeticion(
        @NotBlank String token,

        @NotBlank(message = "Escribe una contraseña")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "Usa 8 caracteres o más, con una mayúscula, un número y un carácter especial")
        String nuevaPassword
) {
}