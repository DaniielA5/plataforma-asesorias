-- V4: tokens para recuperar contrasena y para invalidar JWT al hacer logout.

-- Se guarda el HASH del token de recuperacion, nunca el token en claro.
CREATE TABLE password_reset_tokens (
    id          BIGSERIAL   PRIMARY KEY,
    usuario_id  BIGINT      NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    token_hash  VARCHAR(64) NOT NULL UNIQUE,
    expira_en   TIMESTAMP   NOT NULL,
    usado       BOOLEAN     NOT NULL DEFAULT FALSE,
    creado_en   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_password_reset_usuario ON password_reset_tokens (usuario_id);

-- Lista negra de JWT: el "jti" es el id unico de cada token
CREATE TABLE tokens_revocados (
    id           BIGSERIAL   PRIMARY KEY,
    jti          VARCHAR(64) NOT NULL UNIQUE,
    expira_en    TIMESTAMP   NOT NULL,
    revocado_en  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);