import { useEffect, useState } from 'react'
import Avatar from '../../components/Avatar'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import Spinner from '../../components/Spinner'
import AvisoError from '../../components/AvisoError'
import { validarNombre, validarTelefono } from '../auth/validaciones'
import { miPerfil, editarMiPerfil } from './perfilApi'
import { mensajeError } from '../../utils/errores'
import '../admin/Admin.css'

export default function Perfil() {
  const [perfil, setPerfil] = useState(null)
  const [datos, setDatos] = useState({ nombre: '', telefono: '' })
  const [cargando, setCargando] = useState(true)
  const [errorCarga, setErrorCarga] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [errorGeneral, setErrorGeneral] = useState('')
  const [exito, setExito] = useState('')
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  function cargar() {
    setCargando(true)
    setErrorCarga(false)
    miPerfil()
      .then((p) => {
        setPerfil(p)
        setDatos({ nombre: p.nombre, telefono: p.telefono ?? '' })
      })
      .catch(() => setErrorCarga(true))
      .finally(() => setCargando(false))
  }

  useEffect(cargar, [])

  const errores = {
    nombre: validarNombre(datos.nombre),
    telefono: validarTelefono(datos.telefono),
  }
  const cambiar = (campo) => (e) => {
    setDatos((actual) => ({ ...actual, [campo]: e.target.value }))
    setTocados((actual) => ({ ...actual, [campo]: true }))
  }
  const tocar = (campo) => () => setTocados((actual) => ({ ...actual, [campo]: true }))
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')

  async function guardar(e) {
    e.preventDefault()
    setIntento(true)
    setExito('')
    setErrorGeneral('')
    if (errores.nombre || errores.telefono) return
    setGuardando(true)
    try {
      const actualizado = await editarMiPerfil({ nombre: datos.nombre.trim(), telefono: datos.telefono.trim() })
      setPerfil(actualizado)
      setExito('Perfil actualizado')
    } catch (err) {
      setErrorGeneral(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  if (cargando) return <Spinner mensaje="Cargando tu perfil..." />
  if (errorCarga) return <AvisoError onReintentar={cargar} />

  return (
    <section>
      <h1>Mi perfil</h1>
      <form className="admin__panel" onSubmit={guardar} noValidate>
        <div className="admin__acciones">
          <Avatar nombre={perfil.nombre} tamano={64} />
          <div>
            <strong>{perfil.correo}</strong>
            <div><span className="insignia insignia--neutra">{perfil.rol}</span></div>
          </div>
        </div>

        {exito && <p className="admin__aviso" role="status">{exito}</p>}
        {errorGeneral && <AvisoError mensaje={errorGeneral} />}

        <Input id="p-nombre" etiqueta="Nombre completo" valor={datos.nombre}
          onChange={cambiar('nombre')} onBlur={tocar('nombre')} error={mostrar('nombre')} />
        <Input id="p-telefono" etiqueta="Teléfono" tipo="tel" valor={datos.telefono}
          onChange={cambiar('telefono')} onBlur={tocar('telefono')}
          placeholder="+529512345672" error={mostrar('telefono')} />

        <Boton tipo="submit" deshabilitado={guardando}>{guardando ? 'Guardando...' : 'Guardar cambios'}</Boton>
      </form>
    </section>
  )
}