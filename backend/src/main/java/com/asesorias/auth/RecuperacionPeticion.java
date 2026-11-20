package com.asesorias.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperacionPeticion(
    @NotBlank  @Email String correo) {
}
