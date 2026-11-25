import { useState } from 'react'
import { useNavigate, useSearchParams, Link } from 'react-router-dom'
import Input from '../components/Input'
import Boton from '../components/Boton'
import AvisoError from '../components/AvisoError'
import ReglasPassword from '../components/ReglasPassword'
import { evaluarReglasPassword, passwordValida, validarConfirmacion } from './auth/validaciones'
import { restablecerPassword } from '../api/auth'
import './auth/Formulario.css'

export default function Restablecer() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token') || ''
  const navigate = useNavigate()

  const [password, setPassword] = useState('')
  const [confirmacion, setConfirmacion] = useState('')
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState('')

  const errores = {
    password: passwordValida(password) ? '' : 'La contraseña no cumple todas las reglas',
    confirmacion: validarConfirmacion(password, confirmacion),
  }

  const reglas = evaluarReglasPassword(password)
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')
  const tocar = (campo) => () => setTocados((actual) => ({ ...actual, [campo]: true }))

  async function enviar(evento) {
    evento.preventDefault()
    setIntento(true)
    if (Object.values(errores).some(Boolean)) return
    setError('')
    setEnviando(true)
    try {
      await restablecerPassword(token, password)
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.mensaje || 'No se pudo restablecer la contraseña')
    } finally {
      setEnviando(false)
    }
  }

  if (!token) {
    return (
      <div className="formulario">
        <p className="formulario__texto">
          Este enlace no es válido. <Link to="/recuperar">Pide uno nuevo</Link>.
        </p>
      </div>
    )
  }

  return (
    <form className="formulario" onSubmit={enviar} noValidate>
      <h1 className="formulario__titulo">Crear nueva contraseña</h1>

      {error && <AvisoError mensaje={error} />}

      <div>
        <Input
          id="password"
          etiqueta="Contraseña nueva"
          tipo="password"
          valor={password}
          onChange={(e) => { setPassword(e.target.value); tocar('password')() }}
          onBlur={tocar('password')}
        />
        <ReglasPassword reglas={reglas} activa={Boolean(password) || intento} />
      </div>

      <Input
        id="confirmacion"
        etiqueta="Confirmar contraseña"
        tipo="password"
        valor={confirmacion}
        onChange={(e) => { setConfirmacion(e.target.value); tocar('confirmacion')() }}
        onBlur={tocar('confirmacion')}
        error={mostrar('confirmacion')}
      />

      <Boton tipo="submit" deshabilitado={enviando}>
        {enviando ? 'Guardando...' : 'Guardar nueva contraseña'}
      </Boton>
    </form>
  )
}
