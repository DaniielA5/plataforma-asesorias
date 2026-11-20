import { createContext, useContext, useEffect, useState } from 'react'
import api from '../api/axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  const [cargando, setCargando] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      setCargando(false)
      return
    }
    api
      .get('/auth/me')
      .then((res) => setUsuario(res.data))
      .catch(() => localStorage.removeItem('token'))
      .finally(() => setCargando(false))
  }, [])

  async function login(correo, password) {
    const res = await api.post('/auth/login', { correo, password })
    localStorage.setItem('token', res.data.token)
    setUsuario(res.data.usuario)
  }
  async function registrar(datos) {
    const res = await api.post('/auth/registro', datos)
    localStorage.setItem('token', res.data.token)
    setUsuario(res.data.usuario)
  }

  async function logout() {
    try {
      await api.post('/auth/logout')
    } catch {
      // si el token ya no era válido, igual limpiamos la sesión local
    }
    localStorage.removeItem('token')
    setUsuario(null)
  }

  return (
    <AuthContext.Provider value={{ usuario, cargando, login, logout, registrar }}>
      {children}
    </AuthContext.Provider>
    
  )
}

export function useAuth() {
  return useContext(AuthContext)
}