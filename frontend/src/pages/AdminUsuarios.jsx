import { useEffect, useState } from 'react'
import { listarUsuarios, crearProfesor, cambiarEstadoUsuario } from '../api/usuarios'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Input from '../components/Input'
import ReglasPassword from '../components/ReglasPassword'
import { validarNombre, validarCorreo, validarTelefono, passwordValida, evaluarReglasPassword } from './auth/validaciones'
import './AdminUsuarios.css'

export default function AdminUsuarios() {
  const [pagina, setPagina] = useState(null)
  const [page, setPage] = useState(0)
  const [rol, setRol] = useState('')
  const [q, setQ] = useState('')
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(false)

  const [mostrarForm, setMostrarForm] = useState(false)
  const [nuevo, setNuevo] = useState({ nombre: '', correo: '', telefono: '', password: '' })
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)
  const [creando, setCreando] = useState(false)
  const [errorCrear, setErrorCrear] = useState('')

  useEffect(() => { cargar() }, [page, rol])

  function cargar() {
    setCargando(true)
    setError(false)
    listarUsuarios({ rol, q, page })
      .then(setPagina)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }

  function buscar(evento) {
    evento.preventDefault()
    setPage(0)
    cargar()
  }

  async function alCambiarEstado(usuario) {
    try {
      await cambiarEstadoUsuario(usuario.id, !usuario.activo)
      cargar()
    } catch (err) {
      alert(err.response?.data?.mensaje || 'No se pudo cambiar el estado')
    }
  }

  const erroresNuevo = {
    nombre: validarNombre(nuevo.nombre),
    correo: validarCorreo(nuevo.correo),
    telefono: validarTelefono(nuevo.telefono),
    password: passwordValida(nuevo.password) ? '' : 'La contraseña no cumple todas las reglas',
  }
  const mostrarError = (campo) => (tocados[campo] || intento ? erroresNuevo[campo] : '')
  const tocar = (campo) => () => setTocados((a) => ({ ...a, [campo]: true }))
  const reglas = evaluarReglasPassword(nuevo.password)

  async function alCrearProfesor(evento) {
    evento.preventDefault()
    setIntento(true)
    if (Object.values(erroresNuevo).some(Boolean)) return
    setErrorCrear('')
    setCreando(true)
    try {
      await crearProfesor(nuevo)
      setNuevo({ nombre: '', correo: '', telefono: '', password: '' })
      setTocados({})
      setIntento(false)
      setMostrarForm(false)
      cargar()
    } catch (err) {
      setErrorCrear(err.response?.data?.mensaje || 'No se pudo crear el profesor')
    } finally {
      setCreando(false)
    }
  }

  const columnas = [
    { clave: 'nombre', titulo: 'Nombre' },
    { clave: 'correo', titulo: 'Correo' },
    { clave: 'rol', titulo: 'Rol' },
    { clave: 'telefono', titulo: 'Teléfono' },
    {
      clave: 'activo',
      titulo: 'Estado',
      render: (f) => (
        <span className={f.activo ? 'admin-usuarios__activo' : 'admin-usuarios__inactivo'}>
          {f.activo ? 'Activo' : 'Desactivado'}
        </span>
      ),
    },
    {
      clave: 'acciones',
      titulo: '',
      render: (f) => (
        <Boton variante="secundario" onClick={() => alCambiarEstado(f)}>
          {f.activo ? 'Desactivar' : 'Activar'}
        </Boton>
      ),
    },
  ]

  return (
    <section className="admin-usuarios">
      <header className="admin-usuarios__encabezado">
        <h1>Administración de usuarios</h1>
        <Boton onClick={() => setMostrarForm((v) => !v)}>
          {mostrarForm ? 'Cancelar' : '+ Crear profesor'}
        </Boton>
      </header>

      {mostrarForm && (
        <form className="admin-usuarios__form" onSubmit={alCrearProfesor} noValidate>
          {errorCrear && <AvisoError mensaje={errorCrear} />}
          <Input id="nombre" etiqueta="Nombre" valor={nuevo.nombre}
            onChange={(e) => { setNuevo((n) => ({ ...n, nombre: e.target.value })); tocar('nombre')() }}
            onBlur={tocar('nombre')} error={mostrarError('nombre')} />
          <Input id="correo" etiqueta="Correo" tipo="email" valor={nuevo.correo}
            onChange={(e) => { setNuevo((n) => ({ ...n, correo: e.target.value })); tocar('correo')() }}
            onBlur={tocar('correo')} error={mostrarError('correo')} />
          <Input id="telefono" etiqueta="Teléfono" tipo="tel" valor={nuevo.telefono}
            onChange={(e) => { setNuevo((n) => ({ ...n, telefono: e.target.value })); tocar('telefono')() }}
            onBlur={tocar('telefono')} error={mostrarError('telefono')} />
          <div>
            <Input id="password" etiqueta="Contraseña" tipo="password" valor={nuevo.password}
              onChange={(e) => { setNuevo((n) => ({ ...n, password: e.target.value })); tocar('password')() }}
              onBlur={tocar('password')} />
            <ReglasPassword reglas={reglas} activa={Boolean(nuevo.password) || intento} />
          </div>
          <Boton tipo="submit" deshabilitado={creando}>{creando ? 'Creando...' : 'Crear profesor'}</Boton>
        </form>
      )}

      <form className="admin-usuarios__filtros" onSubmit={buscar}>
        <select value={rol} onChange={(e) => { setPage(0); setRol(e.target.value) }}>
          <option value="">Todos los roles</option>
          <option value="ADMIN">Administrador</option>
          <option value="PROFESOR">Profesor</option>
          <option value="ALUMNO">Alumno</option>
        </select>
        <Input id="buscar" etiqueta="" placeholder="Buscar por nombre o correo" valor={q}
          onChange={(e) => setQ(e.target.value)} />
        <Boton tipo="submit" variante="secundario">Buscar</Boton>
      </form>

      {cargando && <Spinner mensaje="Cargando usuarios..." />}
      {!cargando && error && <AvisoError onReintentar={cargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio="No hay usuarios con ese filtro."
        />
      )}
    </section>
  )
}