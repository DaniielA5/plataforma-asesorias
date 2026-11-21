import api from './axios'

export function solicitarRecuperacion(correo) {
  return api.post('/auth/recuperar', { correo })
}

export function restablecerPassword(token, nuevaPassword) {
  return api.post('/auth/restablecer', { token, nuevaPassword })
}