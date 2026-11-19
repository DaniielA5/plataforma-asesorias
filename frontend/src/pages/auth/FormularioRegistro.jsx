import { useState } from 'react'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import AvisoError from '../../components/AvisoError'
import ReglasPassword from '../../components/ReglasPassword'
import logo from '../../assets/logo.svg'
import {
  evaluarReglasPassword,
  passwordValida,
  validarConfirmacion,
  validarCorreo,
  validarNombre,
  validarTelefono,
} from './validaciones'
import './Formulario.css'

function FormularioRegistro({ onSubmit, enviando = false, errorGeneral = '' }) {
  const [datos, setDatos] = useState({
    nombre: '',
    correo: '',
    telefono: '',
    password: '',
    confirmacion: '',
  })
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  const errores = {
    nombre: validarNombre(datos.nombre),
    correo: validarCorreo(datos.correo),
    telefono: validarTelefono(datos.telefono),
    password: passwordValida(datos.password) ? '' : 'La contraseña no cumple todas las reglas',
    confirmacion: validarConfirmacion(datos.password, datos.confirmacion),
  }

  const reglas = evaluarReglasPassword(datos.password)

  const cambiar = (campo) => (evento) => {
    setDatos((actual) => ({ ...actual, [campo]: evento.target.value }))
    setTocados((actual) => ({ ...actual, [campo]: true }))
  }

  const tocar = (campo) => () => setTocados((actual) => ({ ...actual, [campo]: true }))
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')

  function enviar(evento) {
    evento.preventDefault()
    setIntento(true)
    if (Object.values(errores).some(Boolean)) return
    onSubmit({
      nombre: datos.nombre.trim(),
      correo: datos.correo.trim(),
      telefono: datos.telefono.trim(),
      password: datos.password,
    })
  }

  return (
    <div className="formulario-pagina">
      <form className="formulario" onSubmit={enviar} noValidate>
        <img className="formulario__logo" src={logo} alt="AyudITO" />
        <h1 className="formulario__titulo">Crear cuenta</h1>

        {errorGeneral && <AvisoError mensaje={errorGeneral} />}

        <Input
          id="nombre"
          etiqueta="Nombre completo"
          valor={datos.nombre}
          onChange={cambiar('nombre')}
          onBlur={tocar('nombre')}
          placeholder="Carlos Juárez"
          error={mostrar('nombre')}
        />

        <Input
          id="correo"
          etiqueta="Correo"
          tipo="email"
          valor={datos.correo}
          onChange={cambiar('correo')}
          onBlur={tocar('correo')}
          placeholder="tu@correo.com"
          error={mostrar('correo')}
        />

        <Input
          id="telefono"
          etiqueta="Teléfono"
          tipo="tel"
          valor={datos.telefono}
          onChange={cambiar('telefono')}
          onBlur={tocar('telefono')}
          placeholder="+529512345672"
          error={mostrar('telefono')}
        />

        <div>
          <Input
            id="password"
            etiqueta="Contraseña"
            tipo="password"
            valor={datos.password}
            onChange={cambiar('password')}
            onBlur={tocar('password')}
          />
          <ReglasPassword reglas={reglas} activa={Boolean(datos.password) || intento} />
        </div>

        <Input
          id="confirmacion"
          etiqueta="Confirmar contraseña"
          tipo="password"
          valor={datos.confirmacion}
          onChange={cambiar('confirmacion')}
          onBlur={tocar('confirmacion')}
          error={mostrar('confirmacion')}
        />

        <Boton tipo="submit" deshabilitado={enviando}>
          {enviando ? 'Creando cuenta...' : 'Crear cuenta'}
        </Boton>
      </form>
    </div>
  )
}

export default FormularioRegistro