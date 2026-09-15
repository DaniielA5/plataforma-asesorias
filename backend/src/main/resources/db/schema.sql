-- Schema MVP — plataforma de asesorías
-- MySQL 8+

CREATE DATABASE IF NOT EXISTS asesorias_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE asesorias_db;

DROP TABLE IF EXISTS inscripciones;
DROP TABLE IF EXISTS asesorias;
DROP TABLE IF EXISTS profesor_materia;
DROP TABLE IF EXISTS materias;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  correo         VARCHAR(120) NOT NULL UNIQUE,
  password_hash  VARCHAR(255) NOT NULL,
  nombre         VARCHAR(120) NOT NULL,
  rol            ENUM('ALUMNO','PROFESOR') NOT NULL,
  creado_en      DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE materias (
  id      BIGINT AUTO_INCREMENT PRIMARY KEY,
  codigo  VARCHAR(20)  NOT NULL UNIQUE,
  nombre  VARCHAR(120) NOT NULL
);

CREATE TABLE profesor_materia (
  profesor_id BIGINT NOT NULL,
  materia_id  BIGINT NOT NULL,
  PRIMARY KEY (profesor_id, materia_id),
  FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (materia_id)  REFERENCES materias(id) ON DELETE CASCADE
);

CREATE TABLE asesorias (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  profesor_id  BIGINT NOT NULL,
  materia_id   BIGINT NOT NULL,
  fecha        DATE   NOT NULL,
  hora         TIME   NOT NULL,
  lugar        VARCHAR(120) NOT NULL,
  cupo_max     INT    NOT NULL,
  estado       ENUM('ACTIVA','CANCELADA') NOT NULL DEFAULT 'ACTIVA',
  notas        VARCHAR(500),
  FOREIGN KEY (profesor_id) REFERENCES usuarios(id),
  FOREIGN KEY (materia_id)  REFERENCES materias(id),
  CHECK (cupo_max > 0)
);

CREATE TABLE inscripciones (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  asesoria_id  BIGINT NOT NULL,
  alumno_id    BIGINT NOT NULL,
  inscrito_en  DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (asesoria_id, alumno_id),
  FOREIGN KEY (asesoria_id) REFERENCES asesorias(id) ON DELETE CASCADE,
  FOREIGN KEY (alumno_id)   REFERENCES usuarios(id)
);
