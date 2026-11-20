import { useState } from 'react'
import { solicitarRecuperacion } from '../api/auth'
import FormularioRecuperar from './auth/FormularioRecuperar'

export default function Recuperar() {
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [mensajeExito, setMensajeExito] = useState('')

  async function enviar({ correo }) {
    setError('')
    setMensajeExito('')
    setEnviando(true)
    try {
      await solicitarRecuperacion(correo)
      setMensajeExito('Si ese correo tiene cuenta, te enviamos instrucciones para recuperarla.')
    } catch {
      setError('No se pudo procesar tu solicitud, intenta de nuevo')
    } finally {
      setEnviando(false)
    }
  }

  return (
    <FormularioRecuperar
      onSubmit={enviar}
      enviando={enviando}
      errorGeneral={error}
      mensajeExito={mensajeExito}
    />
  )
}