import { useState } from 'react'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import AvisoError from '../../components/AvisoError'
import { validarCorreo } from './validaciones'
import './Formulario.css'
import { Link } from 'react-router-dom'

function FormularioRecuperar({ onSubmit, enviando = false, errorGeneral = '', mensajeExito = '' }) {
  const [correo, setCorreo] = useState('')
  const [tocado, setTocado] = useState(false)
  const [intento, setIntento] = useState(false)

  const error = validarCorreo(correo)

  function enviar(evento) {
    evento.preventDefault()
    setIntento(true)
    if (error) return
    onSubmit({ correo: correo.trim() })
  }

  return (
    <form className="formulario" onSubmit={enviar} noValidate>
      <h1 className="formulario__titulo">Recuperar contraseña</h1>
      <p className="formulario__texto">
        Escribe el correo de tu cuenta y te enviaremos las instrucciones para crear una contraseña nueva.
      </p>

      {errorGeneral && <AvisoError mensaje={errorGeneral} />}
      {mensajeExito && (
        <p className="formulario__exito" role="status">
          {mensajeExito}
        </p>
      )}

      <Input
        id="correo"
        etiqueta="Correo"
        tipo="email"
        valor={correo}
        onChange={(e) => { setCorreo(e.target.value); setTocado(true) }}
        onBlur={() => setTocado(true)}
        placeholder="tu@correo.com"
        error={tocado || intento ? error : ''}
      />

      <Boton tipo="submit" deshabilitado={enviando}>
        {enviando ? 'Enviando...' : 'Enviar instrucciones'}
      </Boton>

      <div className="formulario__enlaces">
        <Link to="/login">Volver a iniciar sesión</Link>
      </div>
    </form>
  )
}

export default FormularioRecuperar
