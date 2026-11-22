import { useState } from 'react'
import Tabla from '../../components/Tabla'
import Select from '../../components/Select'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import Modal from '../../components/Modal'
import Spinner from '../../components/Spinner'
import AvisoError from '../../components/AvisoError'
import FormularioUsuario from './FormularioUsuario'
import { useListado } from './useListado'
import { cambiarEstadoUsuario, crearProfesor, editarUsuario, listarUsuarios } from './adminApi'
import { mensajeError } from '../../utils/errores'
import { useAuth } from '../../context/AuthContext'
import './Admin.css'

const ROLES = [
  { valor: 'ADMIN', texto: 'Administrador' },
  { valor: 'PROFESOR', texto: 'Profesor' },
  { valor: 'ALUMNO', texto: 'Alumno' },
]

export default function UsuariosAdmin() {
  const { usuario: yo } = useAuth()

  const [rol, setRol] = useState('')
  const [busqueda, setBusqueda] = useState('')
  const [q, setQ] = useState('')
  const { pagina, setPage, cargando, error, recargar } = useListado(listarUsuarios, { rol, q })

  const [formulario, setFormulario] = useState(null)
  const [guardando, setGuardando] = useState(false)
  const [errorFormulario, setErrorFormulario] = useState('')
  const [aConfirmar, setAConfirmar] = useState(null)
  const [aviso, setAviso] = useState('')
  const [errorAccion, setErrorAccion] = useState('')

  function buscar(e) {
    e.preventDefault()
    setQ(busqueda.trim())
    setPage(0)
  }

  function abrirFormulario(nuevo) {
    setAviso('')
    setErrorAccion('')
    setErrorFormulario('')
    setFormulario(nuevo)
  }

  async function guardar(cuerpo) {
    setGuardando(true)
    setErrorFormulario('')
    try {
      if (formulario.modo === 'crear') {
        await crearProfesor(cuerpo)
        setAviso('Profesor creado')
      } else {
        await editarUsuario(formulario.usuario.id, cuerpo)
        setAviso('Usuario actualizado')
      }
      setFormulario(null)
      recargar()
    } catch (err) {
      setErrorFormulario(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  async function confirmarEstado() {
    const usuario = aConfirmar
    setAConfirmar(null)
    setAviso('')
    setErrorAccion('')
    try {
      await cambiarEstadoUsuario(usuario.id, !usuario.activo)
      setAviso(usuario.activo ? 'Usuario desactivado' : 'Usuario activado')
      recargar()
    } catch (err) {
      setErrorAccion(mensajeError(err))
    }
  }

  const columnas = [
    { clave: 'nombre', titulo: 'Nombre' },
    { clave: 'correo', titulo: 'Correo' },
    { clave: 'rol', titulo: 'Rol' },
    { clave: 'telefono', titulo: 'Teléfono', render: (u) => u.telefono || '—' },
    {
      clave: 'activo',
      titulo: 'Estado',
      render: (u) => (
        <span className={u.activo ? 'insignia insignia--ok' : 'insignia insignia--neutra'}>
          {u.activo ? 'Activo' : 'Desactivado'}
        </span>
      ),
    },
    {
      clave: 'acciones',
      titulo: '',
      render: (u) => (
        <div className="admin__acciones">
          <Boton variante="secundario" onClick={() => abrirFormulario({ modo: 'editar', usuario: u })}>
            Editar
          </Boton>
          {u.id !== yo?.id && (
            <Boton variante="secundario" onClick={() => setAConfirmar(u)}>
              {u.activo ? 'Desactivar' : 'Activar'}
            </Boton>
          )}
        </div>
      ),
    },
  ]

  return (
    <div>
      <div className="admin__encabezado">
        <h2>Usuarios</h2>
        <Boton onClick={() => abrirFormulario({ modo: 'crear' })}>Nuevo profesor</Boton>
      </div>

      {aviso && <p className="admin__aviso" role="status">{aviso}</p>}
      {errorAccion && <AvisoError mensaje={errorAccion} />}

      {formulario && (
        <FormularioUsuario
          key={formulario.usuario?.id ?? 'nuevo'}
          modo={formulario.modo}
          inicial={formulario.usuario}
          onGuardar={guardar}
          onCancelar={() => setFormulario(null)}
          guardando={guardando}
          errorGeneral={errorFormulario}
        />
      )}

      <form className="admin__barra" onSubmit={buscar}>
        <Input id="busqueda-usuarios" etiqueta="Buscar" valor={busqueda}
          onChange={(e) => setBusqueda(e.target.value)} placeholder="Nombre o correo" />
        <Select id="filtro-rol" etiqueta="Rol" valor={rol}
          onChange={(e) => { setRol(e.target.value); setPage(0) }}
          opcionVacia="Todos" opciones={ROLES} />
        <Boton tipo="submit">Buscar</Boton>
      </form>

      {cargando && <Spinner mensaje="Cargando usuarios..." />}
      {!cargando && error && <AvisoError onReintentar={recargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio="No hay usuarios con esos filtros."
        />
      )}

      <Modal
        abierto={Boolean(aConfirmar)}
        titulo={aConfirmar?.activo ? 'Desactivar usuario' : 'Activar usuario'}
        mensaje={
          aConfirmar?.activo
            ? `${aConfirmar?.nombre} ya no podrá iniciar sesión. Puedes volver a activarlo cuando quieras.`
            : `${aConfirmar?.nombre} podrá iniciar sesión otra vez.`
        }
        onConfirmar={confirmarEstado}
        onCancelar={() => setAConfirmar(null)}
      />
    </div>
  )
}