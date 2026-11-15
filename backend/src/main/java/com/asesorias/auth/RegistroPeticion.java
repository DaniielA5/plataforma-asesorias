package com.asesorias.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroPeticion(
        @NotBlank(message = "Escribe tu nombre")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String nombre,

        @NotBlank(message = "Escribe tu correo")
        @Email(message = "No es un correo válido")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String correo,

        @NotBlank(message = "Escribe una contraseña")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "Usa 8 caracteres o más, con una mayúscula, un número y un carácter especial")
        String password,

        @NotBlank(message = "Escribe tu teléfono")
        @Pattern(regexp = "^\\+[1-9]\\d{9,14}$",
                message = "Escribe el teléfono con lada, por ejemplo +529512345672")
        String telefono
) {
}