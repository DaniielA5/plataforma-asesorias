import api from "./axios";

export function listarAsesorias({ materiaId, estado, page = 0, size } = {}) {
  return api.get('/asesorias', {
    params: { materiaId: materiaId || undefined, estado: estado || undefined, page, size: size || undefined },
  }).then((res) => res.data)
}

export function inscribirse(asesoriaId) {
  return api.post(`/asesorias/${asesoriaId}/inscripciones`).then((res) => res.data)

}

export function misInscripciones() {
  return api.get('/inscripciones/mias').then((res) => res.data)
}

export function cancelarInscripcion(asesoriaId) {
  return api.delete(`/asesorias/${asesoriaId}/inscripciones`)
}

export function misAsesoriasProfesor(page = 0, size) {
  return api.get('/asesorias/mias', { params: { page, size: size || undefined } }).then((res) => res.data)
}

export function verInscritos(asesoriaId) {
  return api.get(`/asesorias/${asesoriaId}/inscripciones`).then((res) => res.data)
}

export function cancelarAsesoria(asesoriaId) {
  return api.patch(`/asesorias/${asesoriaId}/cancelar`).then((res) => res.data)
}

export function crearAsesoria(cuerpo) {
  return api.post('/asesorias', cuerpo).then((res) => res.data)
}

export function editarAsesoria(id, cuerpo) {
  return api.put(`/asesorias/${id}`, cuerpo).then((res) => res.data)
}