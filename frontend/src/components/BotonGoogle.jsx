import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { cargarGoogle } from '../utils/google'
import { mensajeError } from '../utils/errores'
import AvisoError from './AvisoError'
import './BotonGoogle.css'

const CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID

// texto: 'continue_with' (Login) o 'signup_with' (Registro).
export default function BotonGoogle({ texto = 'continue_with', separador = 'o con tu correo' }) {
  const contenedor = useRef(null)
  const alRecibirCredencial = useRef(null)
  const [error, setError] = useState('')
  const { loginConGoogle } = useAuth()
  const navigate = useNavigate()

  // Google recibe la función una sola vez; la referencia le entrega siempre la versión actual.
  useEffect(() => {
    alRecibirCredencial.current = async ({ credential }) => {
      setError('')
      try {
        await loginConGoogle(credential)
        navigate('/')
      } catch (err) {
        setError(mensajeError(err))
      }
    }
  })

  useEffect(() => {
    if (!CLIENT_ID) return
    let activo = true

    cargarGoogle()
      .then((google) => {
        if (!activo) return
        google.accounts.id.initialize({
          client_id: CLIENT_ID,
          callback: (respuesta) => alRecibirCredencial.current(respuesta),
        })
        google.accounts.id.renderButton(contenedor.current, {
          type: 'standard',
          theme: 'outline',
          size: 'large',
          shape: 'pill',
          logo_alignment: 'center',
          text: texto,
          locale: 'es',
          width: Math.min(contenedor.current.offsetWidth, 400),
        })
      })
      .catch(() => {
        if (activo) setError('No se pudo cargar el botón de Google')
      })

    return () => {
      activo = false
    }
  }, [texto])

  // Sin ID de cliente no se muestra nada: el acceso con correo sigue igual.
  if (!CLIENT_ID) return null

  return (
    <div className="boton-google">
      {error && <AvisoError mensaje={error} />}
      <div ref={contenedor} className="boton-google__boton" />
      <div className="boton-google__separador">{separador}</div>
    </div>
  )
}