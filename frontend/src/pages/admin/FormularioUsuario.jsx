import { useState } from 'react'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import AvisoError from '../../components/AvisoError'
import ReglasPassword from '../../components/ReglasPassword'
import {
  evaluarReglasPassword,
  passwordValida,
  validarCorreo,
  validarNombre,
  validarTelefono,
} from '../auth/validaciones'
import './Admin.css'

export default function FormularioUsuario({ modo, inicial, onGuardar, onCancelar, guardando = false, errorGeneral = '' }) {
  const creando = modo === 'crear'
  const [datos, setDatos] = useState({
    nombre: inicial?.nombre ?? '',
    correo: inicial?.correo ?? '',
    telefono: inicial?.telefono ?? '',
    password: '',
  })
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  const errores = {
    nombre: validarNombre(datos.nombre),
    correo: validarCorreo(datos.correo),
    telefono: validarTelefono(datos.telefono),
    password: creando && !passwordValida(datos.password) ? 'La contraseña no cumple todas las reglas' : '',
  }

  const cambiar = (campo) => (e) => {
    setDatos((actual) => ({ ...actual, [campo]: e.target.value }))
    setTocados((actual) => ({ ...actual, [campo]: true }))
  }
  const tocar = (campo) => () => setTocados((actual) => ({ ...actual, [campo]: true }))
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')

  function enviar(e) {
    e.preventDefault()
    setIntento(true)
    if (Object.values(errores).some(Boolean)) return
    const cuerpo = {
      nombre: datos.nombre.trim(),
      correo: datos.correo.trim(),
      telefono: datos.telefono.trim(),
    }
    if (creando) cuerpo.password = datos.password
    onGuardar(cuerpo)
  }

  return (
    <form className="admin__panel" onSubmit={enviar} noValidate>
      <h3>{creando ? 'Nuevo profesor' : 'Editar usuario'}</h3>
      {errorGeneral && <AvisoError mensaje={errorGeneral} />}

      <Input id="u-nombre" etiqueta="Nombre completo" valor={datos.nombre}
        onChange={cambiar('nombre')} onBlur={tocar('nombre')} error={mostrar('nombre')} />
      <Input id="u-correo" etiqueta="Correo" tipo="email" valor={datos.correo}
        onChange={cambiar('correo')} onBlur={tocar('correo')} error={mostrar('correo')} />
      <Input id="u-telefono" etiqueta="Teléfono" tipo="tel" valor={datos.telefono}
        onChange={cambiar('telefono')} onBlur={tocar('telefono')}
        placeholder="+529512345672" error={mostrar('telefono')} />

      {creando && (
        <div>
          <Input id="u-password" etiqueta="Contraseña" tipo="password" valor={datos.password}
            onChange={cambiar('password')} onBlur={tocar('password')} />
          <ReglasPassword reglas={evaluarReglasPassword(datos.password)} activa={Boolean(datos.password) || intento} />
        </div>
      )}

      <div className="admin__acciones">
        <Boton tipo="submit" deshabilitado={guardando}>{guardando ? 'Guardando...' : 'Guardar'}</Boton>
        <Boton variante="secundario" onClick={onCancelar}>Cancelar</Boton>
      </div>
    </form>
  )
}