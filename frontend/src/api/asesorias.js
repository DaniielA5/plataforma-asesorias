import api from "./axios";

export function listarAsesorias({ materiaId, page =0} = {} ){
    return api.get('/asesorias', {params: {materiaId: materiaId || undefined, page}
    })
        .then((res) => res.data)
}

export function inscribirse(asesoriaId) {
  return api.post(`/asesorias/${asesoriaId}/inscripciones`).then((res) => res.data)

}