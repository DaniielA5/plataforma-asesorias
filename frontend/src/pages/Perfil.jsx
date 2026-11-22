import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { actualizarPerfil } from '../api/usuarios'
import Input from '../components/Input'
import Boton from '../components/Boton'
import AvisoError from '../components/AvisoError'
import { validarNombre, validarTelefono } from './auth/validaciones'
import './Perfil.css'

export default function Perfil() {
  const { usuario, actualizarUsuario } = useAuth()
  const [nombre, setNombre] = useState(usuario.nombre)
  const [telefono, setTelefono] = useState(usuario.telefono || '')
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState('')
  const [guardado, setGuardado] = useState(false)

  const errores = {
    nombre: validarNombre(nombre),
    telefono: validarTelefono(telefono),
  }
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')
  const tocar = (campo) => () => setTocados((a) => ({ ...a, [campo]: true }))

  async function enviar(evento) {
    evento.preventDefault()
    setIntento(true)
    setGuardado(false)
    if (errores.nombre || errores.telefono) return
    setError('')
    setEnviando(true)
    try {
      const actualizado = await actualizarPerfil({ nombre: nombre.trim(), telefono: telefono.trim() })
      actualizarUsuario(actualizado)
      setGuardado(true)
    } catch (err) {
      setError(err.response?.data?.mensaje || 'No se pudo guardar')
    } finally {
      setEnviando(false)
    }
  }

  return (
    <section className="perfil">
      <h1 className="perfil__titulo">Mi perfil</h1>

      <form className="perfil__form" onSubmit={enviar} noValidate>
        {error && <AvisoError mensaje={error} />}
        {guardado && <p className="perfil__exito">Tus datos se guardaron.</p>}

        <div className="perfil__campo-fijo">
          <span className="perfil__etiqueta-fija">Correo</span>
          <span>{usuario.correo}</span>
        </div>
        <div className="perfil__campo-fijo">
          <span className="perfil__etiqueta-fija">Rol</span>
          <span className="perfil__insignia">{usuario.rol}</span>
        </div>

        <Input
          id="nombre"
          etiqueta="Nombre"
          valor={nombre}
          onChange={(e) => { setNombre(e.target.value); tocar('nombre')() }}
          onBlur={tocar('nombre')}
          error={mostrar('nombre')}
        />

        <Input
          id="telefono"
          etiqueta="Teléfono"
          tipo="tel"
          valor={telefono}
          onChange={(e) => { setTelefono(e.target.value); tocar('telefono')() }}
          onBlur={tocar('telefono')}
          error={mostrar('telefono')}
        />

        <Boton tipo="submit" deshabilitado={enviando}>
          {enviando ? 'Guardando...' : 'Guardar cambios'}
        </Boton>
      </form>
    </section>
  )
}