export function mensajeError(err, porDefecto = 'Ocurrió un error, inténtalo de nuevo') {
  const datos = err.response?.data
  if (!datos) return 'No pudimos conectar con el servidor'
  const detalle = datos.errores ? Object.values(datos.errores)[0] : null
  return detalle || datos.mensaje || porDefecto
}