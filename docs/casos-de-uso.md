# Casos de uso

## Actores

- **Alumno** — usuario con correo institucional que busca asesorías.
- **Profesor** — usuario que ofrece asesorías. Su rol es preasignado en BD (no hay auto-registro).

## Casos de uso — MVP (Fase 1)

### CU-01 Iniciar sesión
- Actor: Alumno / Profesor
- Precondición: usuario registrado en la BD.
- Flujo: ingresa correo institucional + contraseña → recibe JWT → es redirigido al dashboard según su rol.

### CU-02 Publicar asesoría
- Actor: Profesor
- Flujo: en su dashboard captura materia, fecha, hora, lugar y cupo máximo. La asesoría queda ACTIVA.

### CU-03 Listar asesorías disponibles
- Actor: Alumno
- Flujo: ve el catálogo de asesorías ACTIVAS con cupo disponible, filtrable por materia.

### CU-04 Inscribirse a una asesoría
- Actor: Alumno
- Precondición: hay cupo. El alumno no está ya inscrito.
- Flujo: da clic en "inscribirme" → se crea inscripción → se decrementa el cupo disponible.

### CU-05 Ver inscritos de una asesoría
- Actor: Profesor
- Flujo: en cada asesoría ve la lista de alumnos inscritos.

### CU-06 Cancelar asesoría
- Actor: Profesor
- Flujo: cambia estado a CANCELADA. Los alumnos inscritos ven la asesoría marcada como cancelada en su dashboard.

### CU-07 Ver "mis próximas asesorías"
- Actor: Alumno / Profesor
- Flujo: dashboard muestra asesorías futuras del usuario (inscritas o publicadas).

### CU-08 Ver historial
- Actor: Alumno / Profesor
- Flujo: dashboard muestra asesorías pasadas.

## Casos de uso — Fase 2 (si sobra tiempo)

- CU-09 Calificar profesor tras una asesoría.
- CU-10 Solicitar asesoría directamente a un profesor.
- CU-11 Notificaciones al alumno cuando el profesor cancela.

## Fuera de alcance (trabajo futuro)

- Registro de profesores con aprobación por administrador.
- Integración con SSO de la universidad.
- Notificaciones por WhatsApp / correo real.
- Chat en tiempo real profesor-alumno.
