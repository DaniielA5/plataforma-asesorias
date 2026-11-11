-- V3: asesorias que publica un profesor y las inscripciones de los alumnos.

CREATE TABLE asesorias (
    id           BIGSERIAL    PRIMARY KEY,
    profesor_id  BIGINT       NOT NULL REFERENCES usuarios (id),
    materia_id   BIGINT       NOT NULL REFERENCES materias (id),
    fecha        DATE         NOT NULL,
    hora         TIME         NOT NULL,
    lugar        VARCHAR(120) NOT NULL,
    cupo_max     INT          NOT NULL CHECK (cupo_max BETWEEN 1 AND 100),
    estado       VARCHAR(20)  NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'CANCELADA')),
    notas        VARCHAR(500),
    creado_en    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_asesorias_profesor ON asesorias (profesor_id);
CREATE INDEX idx_asesorias_materia  ON asesorias (materia_id);
CREATE INDEX idx_asesorias_fecha    ON asesorias (fecha);

-- N:M alumno <-> asesoria, con datos propios (fecha de inscripcion).
CREATE TABLE inscripciones (
    id           BIGSERIAL  PRIMARY KEY,
    asesoria_id  BIGINT     NOT NULL REFERENCES asesorias (id) ON DELETE CASCADE,
    alumno_id    BIGINT     NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    inscrito_en  TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_inscripcion_asesoria_alumno UNIQUE (asesoria_id, alumno_id)
);

CREATE INDEX idx_inscripciones_alumno ON inscripciones (alumno_id);