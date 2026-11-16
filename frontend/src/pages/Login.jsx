import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const [correo, setCorreo] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const { login } = useAuth()
  const navigate = useNavigate()

  async function enviar(e) {
    e.preventDefault()
    setError('')
    try {
      await login(correo, password)
      navigate('/')
    } catch {
      setError('Correo o contraseña incorrectos')
    }
  }

  return (
    <form onSubmit={enviar}>
      <h1>Iniciar sesión</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <input type="email" placeholder="Correo" value={correo} onChange={(e) => setCorreo(e.target.value)} required />
      <input type="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)} required />
      <button type="submit">Entrar</button>
    </form>
  )
}