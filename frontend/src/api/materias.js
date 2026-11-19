import api from './axios'

export function listarMaterias() {
    return api.get('/materias').then((res) => res.data)
}

