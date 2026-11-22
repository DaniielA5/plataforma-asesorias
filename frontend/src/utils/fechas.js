export function formatearFechaHora(valor) {
  if (!valor) return ''
  const [fecha, hora = ''] = String(valor).split('T')
  const [anio, mes, dia] = fecha.split('-')
  return `${dia}/${mes}/${anio} ${hora.slice(0, 5)}`
}