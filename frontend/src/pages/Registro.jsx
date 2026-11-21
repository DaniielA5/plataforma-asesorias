import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import FormularioRegistro from './auth/FormularioRegistro'

export default function Registro() {
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const { registrar } = useAuth()
  const navigate = useNavigate()

  async function enviar(datos) {
    setError('')
    setEnviando(true)
    try {
      await registrar(datos)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.mensaje || 'No se pudo crear la cuenta')
    } finally {
      setEnviando(false)
    }
  }

  return <FormularioRegistro onSubmit={enviar} enviando={enviando} errorGeneral={error} />
}