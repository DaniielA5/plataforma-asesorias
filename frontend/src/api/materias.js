import api from './axios'

export function listarMaterias() {
  return api.get('/materias', { params: { size: 100 } }).then((res) => res.data.content)
}