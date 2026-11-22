import api from './axios'

export function miPerfil() {
  return api.get('/usuarios/me').then((res) => res.data)
}

export function actualizarPerfil(datos) {
  return api.put('/usuarios/me', datos).then((res) => res.data)
}

export function listarUsuarios({ rol, q, page = 0, size } = {}) {
  return api.get('/usuarios', {
    params: { rol: rol || undefined, q: q || undefined, page, size: size || undefined },
  }).then((res) => res.data)
}

export function crearProfesor(datos) {
  return api.post('/usuarios', datos).then((res) => res.data)
}

export function cambiarEstadoUsuario(id, activo) {
  const accion = activo ? 'activar' : 'desactivar'
  return api.patch(`/usuarios/${id}/${accion}`).then((res) => res.data)
}