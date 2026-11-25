-- V7: enlace con la cuenta de Google.
-- google_sub es el identificador estable que Google da a cada cuenta (claim "sub").
-- Queda en NULL para quien solo entra con correo y contraseña.
ALTER TABLE usuarios ADD COLUMN google_sub VARCHAR(255) UNIQUE;