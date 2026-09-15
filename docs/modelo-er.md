# Modelo Entidad-Relación

## Diagrama

```mermaid
erDiagram
    USUARIOS ||--o{ ASESORIAS : "publica (profesor)"
    USUARIOS ||--o{ INSCRIPCIONES : "se inscribe (alumno)"
    ASESORIAS ||--o{ INSCRIPCIONES : "tiene"
    MATERIAS  ||--o{ ASESORIAS : "es de"
    USUARIOS  ||--o{ PROFESOR_MATERIA : "imparte"
    MATERIAS  ||--o{ PROFESOR_MATERIA : "impartida por"

    USUARIOS {
      bigint  id PK
      string  correo UK
      string  password_hash
      string  nombre
      string  rol "ALUMNO|PROFESOR"
      datetime creado_en
    }

    MATERIAS {
      bigint id PK
      string codigo UK
      string nombre
    }

    PROFESOR_MATERIA {
      bigint profesor_id FK
      bigint materia_id  FK
    }

    ASESORIAS {
      bigint  id PK
      bigint  profesor_id FK
      bigint  materia_id  FK
      date    fecha
      time    hora
      string  lugar
      int     cupo_max
      string  estado "ACTIVA|CANCELADA"
      string  notas
    }

    INSCRIPCIONES {
      bigint  id PK
      bigint  asesoria_id FK
      bigint  alumno_id   FK
      datetime inscrito_en
    }
```

## Reglas de negocio

1. Un profesor solo puede impartir materias que tenga asignadas en `profesor_matiera`.
2. Un alumno no puede inscribirse dos veces a la misma asesoría (`UNIQUE(asesoria_id, alumno_id)`).
3. No se aceptan inscripciones si `COUNT(inscripciones) >= cupo_max`.
4. Solo el profesor dueño puede modificar o cancelar su asesoría.
5. Una asesoría CANCELADA no admite nuevas inscripciones.
