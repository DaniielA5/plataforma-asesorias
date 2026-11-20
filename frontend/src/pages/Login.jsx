import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import FormularioLogin from './auth/FormularioLogin'

export default function Login() {
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  async function enviar({ correo, password }) {
    setError('')
    setEnviando(true)
    try {
      await login(correo, password)
      navigate('/')
    } catch {
      setError('Correo o contraseña incorrectos')
    } finally {
      setEnviando(false)
    }
  }

  return <FormularioLogin onSubmit={enviar} enviando={enviando} errorGeneral={error} />
}