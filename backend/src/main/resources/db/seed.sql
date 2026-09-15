-- Datos de prueba
-- Passwords: los hashes de abajo corresponden a "123456" (BCrypt).
-- Reemplazar al implementar auth real.

USE asesorias_db;

INSERT INTO materias (codigo, nombre) VALUES
  ('MAT101', 'Cálculo Diferencial'),
  ('MAT102', 'Álgebra Lineal'),
  ('PRG201', 'Programación Web'),
  ('PRG202', 'Estructura de Datos');

-- Profesores (rol PROFESOR)
INSERT INTO usuarios (correo, password_hash, nombre, rol) VALUES
  ('prof.garcia@utm.edu.mx', '$2a$10$abcdefghijklmnopqrstuv', 'María García',    'PROFESOR'),
  ('prof.lopez@utm.edu.mx',  '$2a$10$abcdefghijklmnopqrstuv', 'Juan López',      'PROFESOR');

-- Alumnos
INSERT INTO usuarios (correo, password_hash, nombre, rol) VALUES
  ('alumno1@utm.edu.mx', '$2a$10$abcdefghijklmnopqrstuv', 'Ana Pérez',    'ALUMNO'),
  ('alumno2@utm.edu.mx', '$2a$10$abcdefghijklmnopqrstuv', 'Luis Ramírez', 'ALUMNO'),
  ('alumno3@utm.edu.mx', '$2a$10$abcdefghijklmnopqrstuv', 'Sofía Cruz',   'ALUMNO');

-- Qué materia imparte cada profe
INSERT INTO profesor_materia (profesor_id, materia_id) VALUES
  (1, 1), (1, 2),      -- María: Cálculo y Álgebra
  (2, 3), (2, 4);      -- Juan: Prog Web y Estructura de Datos

-- Asesorías de ejemplo
INSERT INTO asesorias (profesor_id, materia_id, fecha, hora, lugar, cupo_max, estado, notas) VALUES
  (1, 1, '2026-09-20', '10:00:00', 'Aula B-201', 5, 'ACTIVA', 'Repaso de derivadas'),
  (1, 2, '2026-09-22', '12:00:00', 'Aula B-105', 4, 'ACTIVA', 'Sistemas de ecuaciones'),
  (2, 3, '2026-09-21', '16:00:00', 'Lab de Cómputo 3', 6, 'ACTIVA', 'React básico');

-- Inscripciones de ejemplo
INSERT INTO inscripciones (asesoria_id, alumno_id) VALUES
  (1, 3), (1, 4),
  (3, 3);
