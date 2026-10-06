
DROP TABLE IF EXISTS inscripciones      CASCADE;
DROP TABLE IF EXISTS asesorias          CASCADE;
DROP TABLE IF EXISTS profesor_materia   CASCADE;
DROP TABLE IF EXISTS materias           CASCADE;
DROP TABLE IF EXISTS usuarios           CASCADE;

DROP TYPE IF EXISTS rol_usuario;
DROP TYPE IF EXISTS estado_asesoria;

CREATE TABLE usuarios (
  id             BIGSERIAL PRIMARY KEY,
  correo         VARCHAR(120) NOT NULL UNIQUE,
  password_hash  VARCHAR(255) NOT NULL,
  nombre         VARCHAR(120) NOT NULL,
  rol            VARCHAR(20)  NOT NULL CHECK (rol IN ('ALUMNO','PROFESOR')),
  creado_en      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE materias (
  id      BIGSERIAL PRIMARY KEY,
  codigo  VARCHAR(20)  NOT NULL UNIQUE,
  nombre  VARCHAR(120) NOT NULL
);

CREATE TABLE profesor_materia (
  profesor_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
  materia_id  BIGINT NOT NULL REFERENCES materias(id) ON DELETE CASCADE,
  PRIMARY KEY (profesor_id, materia_id)
);

CREATE TABLE asesorias (
  id           BIGSERIAL PRIMARY KEY,
  profesor_id  BIGINT NOT NULL REFERENCES usuarios(id),
  materia_id   BIGINT NOT NULL REFERENCES materias(id),
  fecha        DATE   NOT NULL,
  hora         TIME   NOT NULL,
  lugar        VARCHAR(120) NOT NULL,
  cupo_max     INT    NOT NULL CHECK (cupo_max > 0),
  estado       VARCHAR(20) NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA','CANCELADA')),
  notas        VARCHAR(500)
);

CREATE TABLE inscripciones (
  id           BIGSERIAL PRIMARY KEY,
  asesoria_id  BIGINT NOT NULL REFERENCES asesorias(id) ON DELETE CASCADE,
  alumno_id    BIGINT NOT NULL REFERENCES usuarios(id),
  inscrito_en  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (asesoria_id, alumno_id)
);