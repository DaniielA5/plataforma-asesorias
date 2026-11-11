-- V2: catalogo de materias y la relacion N:M profesor <-> materia.

CREATE TABLE materias (
    id      BIGSERIAL    PRIMARY KEY,
    codigo  VARCHAR(20)  NOT NULL UNIQUE,
    nombre  VARCHAR(120) NOT NULL
);

-- Tabla puente: un profesor imparte muchas materias y una materia la imparten muchos profesores.
CREATE TABLE profesor_materia (
    profesor_id  BIGINT NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    materia_id   BIGINT NOT NULL REFERENCES materias (id) ON DELETE CASCADE,
    PRIMARY KEY (profesor_id, materia_id)
);

CREATE INDEX idx_profesor_materia_materia ON profesor_materia (materia_id);