import { useState } from 'react'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import AvisoError from '../../components/AvisoError'
import logo from '../../assets/logo.svg'
import { validarCorreo } from './validaciones'
import './Formulario.css'

function FormularioLogin({ onSubmit, enviando = false, errorGeneral = '' }) {
  const [correo, setCorreo] = useState('')
  const [password, setPassword] = useState('')
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  const errores = {
    correo: validarCorreo(correo),
    password: password ? '' : 'La contraseña es obligatoria',
  }

  const tocar = (campo) => setTocados((actual) => ({ ...actual, [campo]: true }))
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')

  function enviar(evento) {
    evento.preventDefault()
    setIntento(true)
    if (errores.correo || errores.password) return
    onSubmit({ correo: correo.trim(), password })
  }

  return (
    <div className="formulario-pagina">
      <form className="formulario" onSubmit={enviar} noValidate>
        <img className="formulario__logo" src={logo} alt="AyudITO" />
        <h1 className="formulario__titulo">Iniciar sesión</h1>

        {errorGeneral && <AvisoError mensaje={errorGeneral} />}

        <Input
          id="correo"
          etiqueta="Correo"
          tipo="email"
          valor={correo}
          onChange={(e) => { setCorreo(e.target.value); tocar('correo') }}
          onBlur={() => tocar('correo')}
          placeholder="tu@correo.com"
          error={mostrar('correo')}
        />

        <Input
          id="password"
          etiqueta="Contraseña"
          tipo="password"
          valor={password}
          onChange={(e) => { setPassword(e.target.value); tocar('password') }}
          onBlur={() => tocar('password')}
          error={mostrar('password')}
        />

        <Boton tipo="submit" deshabilitado={enviando}>
          {enviando ? 'Entrando...' : 'Entrar'}
        </Boton>
      </form>
    </div>
  )
}

export default FormularioLogin