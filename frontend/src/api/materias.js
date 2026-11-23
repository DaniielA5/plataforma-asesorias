import api from './axios'

export function listarMaterias() {
    return api.get('/materias', { params: { size: 100 } }).then((res) => res.data.content)
}

export function materiasDeProfesor(profesorId) {
  return api.get(`/materias/profesores/${profesorId}`).then((res) => res.data)
}
