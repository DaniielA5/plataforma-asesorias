import api from './axios'

export function miPerfil() {
  return api.get('/usuarios/me').then((res) => res.data)
}

export function actualizarPerfil(datos) {
  return api.put('/usuarios/me', datos).then((res) => res.data)
}