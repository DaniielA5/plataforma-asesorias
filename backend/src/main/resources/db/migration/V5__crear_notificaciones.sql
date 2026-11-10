-- V5: registro de cada correo, SMS o WhatsApp que manda el sistema.

CREATE TABLE notificaciones (
    id          BIGSERIAL    PRIMARY KEY,
    usuario_id  BIGINT       REFERENCES usuarios (id) ON DELETE SET NULL,
    canal       VARCHAR(10)  NOT NULL CHECK (canal IN ('EMAIL', 'SMS', 'WHATSAPP')),
    destino     VARCHAR(120) NOT NULL,
    asunto      VARCHAR(200),
    estado      VARCHAR(10)  NOT NULL CHECK (estado IN ('ENVIADA', 'FALLIDA')),
    error       VARCHAR(500),
    enviada_en  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notificaciones_usuario ON notificaciones (usuario_id);
CREATE INDEX idx_notificaciones_canal   ON notificaciones (canal);