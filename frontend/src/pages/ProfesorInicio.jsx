import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { misAsesoriasProfesor } from '../api/asesorias'
import { materiasDeProfesor } from '../api/materias'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import EstadoVacio from '../components/EstadoVacio'
import './Inicio.css'
import './ProfesorInicio.css'

function saludo() {
  const hora = new Date().getHours()
  if (hora < 12) return 'Buenos días'
  if (hora < 19) return 'Buenas tardes'
  return 'Buenas noches'
}

function cuando(asesoria) {
  return new Date(`${asesoria.fecha}T${asesoria.hora}`)
}

export default function ProfesorInicio() {
  const { usuario } = useAuth()
  const [datos, setDatos] = useState(null)
  const [error, setError] = useState(false)

  useEffect(() => {
    Promise.all([
      misAsesoriasProfesor(0, 100),
      materiasDeProfesor(usuario.id),
    ])
      .then(([pagina, materias]) => setDatos({ asesorias: pagina.content, materias }))
      .catch(() => setError(true))
  }, [usuario.id])

  if (error) return <AvisoError onReintentar={() => window.location.reload()} />
  if (!datos) return <Spinner mensaje="Cargando tu panel..." />

  const ahora = new Date()
  const proximas = datos.asesorias
    .filter((a) => a.estado === 'ACTIVA' && cuando(a) >= ahora)
    .sort((a, b) => cuando(a) - cuando(b))

  const siguiente = proximas[0]
  const alumnosInscritos = proximas.reduce((suma, a) => suma + a.inscritos, 0)
  const cuposLibres = proximas.reduce((suma, a) => suma + (a.cupoMax - a.inscritos), 0)
  const llenas = proximas.filter((a) => a.inscritos >= a.cupoMax).length

  return (
    <section className="inicio">
      <header>
        <h1 className="inicio__titulo">{saludo()}, {usuario.nombre.split(' ')[0]}.</h1>
        <p className="inicio__subtitulo">
          {proximas.length > 0
            ? `Tienes ${proximas.length} asesoría${proximas.length === 1 ? '' : 's'} por impartir.`
            : 'No tienes asesorías próximas todavía.'}
        </p>
      </header>

      <div className="profesor-inicio__acciones">
        <Link to="/panel-profesor"><Boton>Gestionar mis asesorías</Boton></Link>
        <Link to="/asesorias"><Boton variante="secundario">Ver catálogo</Boton></Link>
      </div>

      {siguiente ? (
        <div className="inicio__destacada">
          <span className="inicio__etiqueta">Tu próxima asesoría</span>
          <h2 className="inicio__destacada-titulo">{siguiente.materiaNombre}</h2>
          <p className="inicio__destacada-detalle">
            {siguiente.fecha} · {siguiente.hora.slice(0, 5)} · {siguiente.lugar}
          </p>
          <p className="inicio__destacada-profesor">
            {siguiente.inscritos} de {siguiente.cupoMax} lugares ocupados
          </p>
          <Link to="/panel-profesor"><Boton>Ver inscritos</Boton></Link>
        </div>
      ) : (
        <EstadoVacio
          icono="🗓️"
          titulo="No tienes asesorías próximas"
          mensaje="Publica una para que los alumnos puedan inscribirse."
          textoAccion="Crear asesoría"
          rutaAccion="/panel-profesor"
        />
      )}

      <div className="inicio__stats">
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{proximas.length}</span>
          <span className="inicio__stat-etiqueta">Asesorías por venir</span>
        </div>
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{alumnosInscritos}</span>
          <span className="inicio__stat-etiqueta">Alumnos inscritos</span>
        </div>
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{cuposLibres}</span>
          <span className="inicio__stat-etiqueta">Cupos libres</span>
        </div>
        <div className="inicio__stat">
          <span className="inicio__stat-numero">{llenas}</span>
          <span className="inicio__stat-etiqueta">Con cupo lleno</span>
        </div>
      </div>

      <div className="profesor-inicio__columnas">
        <div className="profesor-inicio__panel">
          <h2>Tu agenda</h2>
          {proximas.length === 0 ? (
            <p className="profesor-inicio__vacio">Todavía no hay nada en tu agenda.</p>
          ) : (
            <ul className="profesor-inicio__agenda">
              {proximas.slice(0, 5).map((a) => (
                <li key={a.id} className="profesor-inicio__item">
                  <span className="profesor-inicio__fecha">{a.fecha} · {a.hora.slice(0, 5)}</span>
                  <span className="profesor-inicio__detalle">{a.materiaNombre} ({a.lugar})</span>
                  <span
                    className={
                      a.inscritos >= a.cupoMax
                        ? 'profesor-inicio__cupo profesor-inicio__cupo--lleno'
                        : 'profesor-inicio__cupo'
                    }
                  >
                    {a.inscritos}/{a.cupoMax}
                  </span>
                </li>
              ))}
            </ul>
          )}
          <Link to="/panel-profesor" className="inicio__enlace-catalogo">Ver todas mis asesorías →</Link>
        </div>

        <div className="profesor-inicio__panel">
          <h2>Tus materias</h2>
          {datos.materias.length === 0 ? (
            <p className="profesor-inicio__vacio">
              Todavía no tienes materias asignadas. Pídele al administrador que te las asigne.
            </p>
          ) : (
            <div className="profesor-inicio__materias">
              {datos.materias.map((m) => (
                <span key={m.id} className="profesor-inicio__materia">{m.nombre}</span>
              ))}
            </div>
          )}
        </div>
      </div>
    </section>
  )
}