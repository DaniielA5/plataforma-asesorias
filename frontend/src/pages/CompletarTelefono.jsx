import { useState } from 'react'
import { Navigate } from 'react-router-dom'
import Input from '../components/Input'
import Boton from '../components/Boton'
import AvisoError from '../components/AvisoError'
import { useAuth } from '../context/AuthContext'
import { editarMiPerfil } from './perfil/perfilApi'
import { validarTelefono } from './auth/validaciones'
import { mensajeError } from '../utils/errores'
import { faltaTelefono } from '../utils/usuario'
import './auth/Formulario.css'

export default function CompletarTelefono() {
  const { usuario, cargando, actualizarUsuario, logout } = useAuth()
  const [telefono, setTelefono] = useState('')
  const [intento, setIntento] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [errorGeneral, setErrorGeneral] = useState('')

  if (cargando) return <p className="formulario__texto">Cargando...</p>
  if (!usuario) return <Navigate to="/login" replace />
  // Cuando ya hay teléfono (o no hace falta), esta pantalla no se muestra.
  if (!faltaTelefono(usuario)) return <Navigate to="/" replace />

  const error = validarTelefono(telefono)

  async function guardar(evento) {
    evento.preventDefault()
    setIntento(true)
    if (error) return
    setErrorGeneral('')
    setEnviando(true)
    try {
      // Se reutiliza "editar mi perfil", que pide nombre y teléfono.
      const actualizado = await editarMiPerfil({ nombre: usuario.nombre, telefono: telefono.trim() })
      actualizarUsuario(actualizado)
    } catch (err) {
      setErrorGeneral(mensajeError(err))
      setEnviando(false)
    }
  }

  return (
    <form className="formulario" onSubmit={guardar} noValidate>
      <h1 className="formulario__titulo">Un paso más</h1>
      <p className="formulario__texto">
        Escribe tu número de WhatsApp. Lo usaremos para los avisos de tus asesorías.
      </p>

      {errorGeneral && <AvisoError mensaje={errorGeneral} />}

      <Input
        id="telefono"
        etiqueta="Teléfono (WhatsApp)"
        tipo="tel"
        valor={telefono}
        onChange={(e) => setTelefono(e.target.value)}
        placeholder="+529512345672"
        error={intento ? error : ''}
      />

      <Boton tipo="submit" deshabilitado={enviando}>
        {enviando ? 'Guardando...' : 'Guardar y entrar'}
      </Boton>

      <Boton variante="secundario" onClick={logout}>
        Cerrar sesión
      </Boton>
    </form>
  )
}
