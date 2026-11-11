-- V1: roles y usuarios.
-- Los roles son un catalogo fijo: se insertan aqui porque la app no funciona sin ellos.

CREATE TABLE roles (
    id      BIGSERIAL   PRIMARY KEY,
    nombre  VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO roles (nombre) VALUES ('ADMIN'), ('PROFESOR'), ('ALUMNO');

CREATE TABLE usuarios (
    id             BIGSERIAL    PRIMARY KEY,
    rol_id         BIGINT       NOT NULL REFERENCES roles (id),
    nombre         VARCHAR(120) NOT NULL,
    correo         VARCHAR(120) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    telefono       VARCHAR(20),
    avatar_url     VARCHAR(255),
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuarios_rol ON usuarios (rol_id);