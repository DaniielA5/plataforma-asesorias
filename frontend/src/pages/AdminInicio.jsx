import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { listarAsesorias } from '../api/asesorias'
import { listarUsuarios } from '../api/usuarios'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import './AdminInicio.css'

export default function AdminInicio() {
  const { usuario } = useAuth()
  const [datos, setDatos] = useState(null)
  const [error, setError] = useState(false)

  useEffect(() => {
    Promise.all([
      listarUsuarios({ rol: 'ALUMNO', size: 1 }),
      listarUsuarios({ rol: 'PROFESOR', size: 1 }),
      listarAsesorias({ estado: 'ACTIVA', size: 6 }),
      listarAsesorias({ estado: 'CANCELADA', size: 5 }),
    ])
      .then(([alumnos, profesores, activas, canceladas]) => {
        setDatos({
          totalAlumnos: alumnos.totalElements,
          totalProfesores: profesores.totalElements,
          totalActivas: activas.totalElements,
          proximas: activas.content,
          canceladas: canceladas.content,
        })
      })
      .catch(() => setError(true))
  }, [])

  if (error) return <AvisoError onReintentar={() => window.location.reload()} />
  if (!datos) return <Spinner mensaje="Cargando el panel..." />

  return (
    <section className="admin-inicio">
      <header className="admin-inicio__encabezado">
        <h1 className="admin-inicio__titulo">Hola, {usuario.nombre.split(' ')[0]}.</h1>
        <p className="admin-inicio__subtitulo">Esto es lo que está pasando en la plataforma hoy.</p>
      </header>

      <div className="admin-inicio__acciones">
        <Link to="/admin"><Boton>Administrar usuarios</Boton></Link>
        <Link to="/asesorias"><Boton variante="secundario">Ver catálogo de asesorías</Boton></Link>
      </div>

      <div className="admin-inicio__stats">
        <div className="admin-inicio__stat">
          <span className="admin-inicio__stat-numero">{datos.totalAlumnos}</span>
          <span className="admin-inicio__stat-etiqueta">Alumnos registrados</span>
        </div>
        <div className="admin-inicio__stat">
          <span className="admin-inicio__stat-numero">{datos.totalProfesores}</span>
          <span className="admin-inicio__stat-etiqueta">Profesores</span>
        </div>
        <div className="admin-inicio__stat">
          <span className="admin-inicio__stat-numero">{datos.totalActivas}</span>
          <span className="admin-inicio__stat-etiqueta">Asesorías activas</span>
        </div>
      </div>

      <div className="admin-inicio__columnas">
        <div className="admin-inicio__panel">
          <h2>Próximas asesorías</h2>
          {datos.proximas.length === 0 ? (
            <p className="admin-inicio__vacio">No hay asesorías activas próximas.</p>
          ) : (
            <ul className="admin-inicio__agenda">
              {datos.proximas.map((a) => (
                <li key={a.id} className="admin-inicio__agenda-item">
                  <span className="admin-inicio__agenda-fecha">{a.fecha} · {a.hora}</span>
                  <span className="admin-inicio__agenda-detalle">
                    {a.materiaNombre} — {a.profesorNombre} ({a.lugar})
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="admin-inicio__panel">
          <h2>Asesorías canceladas</h2>
          {datos.canceladas.length === 0 ? (
            <p className="admin-inicio__vacio">No hay asesorías canceladas por ahora.</p>
          ) : (
            <ul className="admin-inicio__agenda admin-inicio__agenda--cancelada">
              {datos.canceladas.map((a) => (
                <li key={a.id} className="admin-inicio__agenda-item">
                  <span className="admin-inicio__agenda-fecha">{a.fecha} · {a.hora}</span>
                  <span className="admin-inicio__agenda-detalle">
                    {a.materiaNombre} — {a.profesorNombre}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </section>
  )
}