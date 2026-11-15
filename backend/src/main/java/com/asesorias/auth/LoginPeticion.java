package com.asesorias.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPeticion(
        @NotBlank(message = "Escribe tu correo")
        @Email(message = "No es un correo válido")
        String correo,

        @NotBlank(message = "Escribe tu contraseña")
        String password
) {
}