// Los avisos por WhatsApp y SMS llegan a los alumnos, y Google no nos da su teléfono.
export function faltaTelefono(usuario) {
  return usuario?.rol === 'ALUMNO' && !usuario.telefono
}