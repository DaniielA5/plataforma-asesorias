import api from '../../api/axios'

const datos = (res) => res.data

const limpiar = (params = {}) =>
  Object.fromEntries(Object.entries(params).filter(([, v]) => v !== '' && v !== null && v !== undefined))

export const listarUsuarios = (params) => api.get('/usuarios', { params: limpiar(params) }).then(datos)
export const crearProfesor = (cuerpo) => api.post('/usuarios', cuerpo).then(datos)
export const editarUsuario = (id, cuerpo) => api.put(`/usuarios/${id}`, cuerpo).then(datos)
export const cambiarEstadoUsuario = (id, activo) =>
  api.patch(`/usuarios/${id}/${activo ? 'activar' : 'desactivar'}`).then(datos)

export const listarMaterias = (params) => api.get('/materias', { params: limpiar(params) }).then(datos)
export const crearMateria = (cuerpo) => api.post('/materias', cuerpo).then(datos)
export const editarMateria = (id, cuerpo) => api.put(`/materias/${id}`, cuerpo).then(datos)
export const eliminarMateria = (id) => api.delete(`/materias/${id}`)

export const listarNotificaciones = (params) =>
  api.get('/notificaciones', { params: limpiar(params) }).then(datos)