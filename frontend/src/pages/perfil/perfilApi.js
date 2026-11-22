import api from '../../api/axios'

export const miPerfil = () => api.get('/usuarios/me').then((res) => res.data)
export const editarMiPerfil = (cuerpo) => api.put('/usuarios/me', cuerpo).then((res) => res.data)