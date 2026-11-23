import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { misInscripciones } from '../api/asesorias'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import './Inicio.css'
import AdminInicio from './AdminInicio'
import EstadoVacio from '../components/EstadoVacio'

function saludo() {
  const hora = new Date().getHours()
  if (hora < 12) return 'Buenos días'
  if (hora < 19) return 'Buenas tardes'
  return 'Buenas noches'
}

export default function Inicio() {
  const { usuario } = useAuth()
  const [inscripciones, setInscripciones] = useState(null)
  const [error, setError] = useState(false)

  useEffect(() => {
    if (usuario?.rol !== 'ALUMNO') return
    misInscripciones()
      .then(setInscripciones)
      .catch(() => setError(true))
  }, [usuario])
  if (usuario?.rol === 'ADMIN') {
    return <AdminInicio />
  }

  if (usuario?.rol !== 'ALUMNO') {
    return (
      <section className="inicio">
        <h1 className="inicio__titulo">Hola, {usuario?.nombre}.</h1>
        <p className="inicio__subtitulo">Plataforma de Asesorías</p>
      </section>
    )
  }

  if (error) {
    return <AvisoError onReintentar={() => window.location.reload()} />
  }

  if (!inscripciones) {
    return <Spinner mensaje="Cargando tu panel..." />
  }

  const activas = inscripciones.filter((i) => i.asesoria.estado === 'ACTIVA')
  const ahora = new Date()
  const proximas = activas
    .filter((i) => new Date(`${i.asesoria.fecha}T${i.asesoria.hora}`) >= ahora)
    .sort((a, b) => new Date(`${a.asesoria.fecha}T${a.asesoria.hora}`) - new Date(`${b.asesoria.fecha}T${b.asesoria.hora}`))

  const siguiente = proximas[0]
  const materiasDistintas = new Set(activas.map((i) => i.asesoria.materiaNombre)).size

  const columnas = [
    { clave: 'materia', titulo: 'Materia', render: (f) => f.asesoria.materiaNombre },
    { clave: 'profesor', titulo: 'Profesor', render: (f) => f.asesoria.profesorNombre },
    { clave: 'fecha', titulo: 'Fecha', render: (f) => f.asesoria.fecha },
    { clave: 'hora', titulo: 'Hora', render: (f) => f.asesoria.hora },
    { clave: 'lugar', titulo: 'Lugar', render: (f) => f.asesoria.lugar },
  ]

  return (
    <section className="inicio">
      <header className="inicio__encabezado">
        <h1 className="inicio__titulo">{saludo()}, {usuario.nombre.split(' ')[0]}.</h1>
        <p className="inicio__subtitulo">
          {proximas.length > 0
            ? `Tienes ${proximas.length} asesoría${proximas.length === 1 ? '' : 's'} por venir.`
            : 'No tienes asesorías próximas todavía.'}
        </p>
      </header>

      {siguiente && (
        <div className="inicio__destacada">
          <span className="inicio__etiqueta">Próxima asesoría</span>
          <h2 className="inicio__destacada-titulo">{siguiente.asesoria.materiaNombre}</h2>
          <p className="inicio__destacada-detalle">
            {siguiente.asesoria.fecha} · {siguiente.asesoria.hora} · {siguiente.asesoria.lugar}
          </p>
          <p className="inicio__destacada-profesor">Con {siguiente.asesoria.profesorNombre}</p>
          <Link to="/mis-asesorias"><Boton>Ver mis asesorías</Boton></Link>
        </div>
      )}

      <div className="inicio__stats">
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{activas.length}</span>
          <span className="inicio__stat-etiqueta">Asesorías inscritas</span>
        </div>
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{materiasDistintas}</span>
          <span className="inicio__stat-etiqueta">Materias distintas</span>
        </div>
      </div>

      <div className="inicio__tabla">
        <h2>Tus próximas asesorías</h2>
        <Tabla
          columnas={columnas}
          filas={proximas}
          page={0}
          totalPages={1}
          onPageChange={() => {}}
          mensajeVacio={<EstadoVacio icono="🗓️" titulo="No tienes asesorías próximas" textoAccion="Ver catálogo" rutaAccion="/asesorias" />}
        />
      </div>

      <Link to="/asesorias" className="inicio__enlace-catalogo">Ver el catálogo completo →</Link>
    </section>
  )
}