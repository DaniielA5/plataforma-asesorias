package com.asesorias.auth;

import jakarta.validation.constraints.NotBlank;

public record GooglePeticion(
        @NotBlank(message = "Falta la credencial de Google")
        String credencial
) {
}