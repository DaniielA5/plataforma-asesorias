import { useEffect, useState } from 'react'
import { misAsesoriasProfesor, verInscritos, cancelarAsesoria, crearAsesoria, editarAsesoria } from '../api/asesorias'
import { materiasDeProfesor } from '../api/materias'
import { useAuth } from '../context/AuthContext'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Modal from '../components/Modal'
import EstadoVacio from '../components/EstadoVacio'
import Avatar from '../components/Avatar'
import FormularioAsesoria from './FormularioAsesoria'
import { useToast } from '../context/ToastContext'
import { mensajeError } from '../utils/errores'
import { formatearFechaHora } from '../utils/fechas'
import './admin/Admin.css'
import './PanelProfesor.css'

export default function PanelProfesor() {
  const { mostrarToast } = useToast()
  const { usuario } = useAuth()
  const [pagina, setPagina] = useState(null)
  const [page, setPage] = useState(0)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(false)

  const [seleccionada, setSeleccionada] = useState(null)
  const [inscritos, setInscritos] = useState([])
  const [cargandoInscritos, setCargandoInscritos] = useState(false)

  const [porCancelar, setPorCancelar] = useState(null)

  const [materias, setMaterias] = useState([])
  const [formulario, setFormulario] = useState(null)
  const [guardando, setGuardando] = useState(false)
  const [errorFormulario, setErrorFormulario] = useState('')
  const [errorAccion, setErrorAccion] = useState('')

  useEffect(() => {
    cargar()
  }, [page])

useEffect(() => {
    if (!usuario?.id) return
    materiasDeProfesor(usuario.id).then(setMaterias).catch(() => {})
  }, [usuario?.id])

  function cargar() {
    setCargando(true)
    setError(false)
    misAsesoriasProfesor(page)
      .then(setPagina)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }

  function abrirFormulario(nuevo) {
    setErrorAccion('')
    setErrorFormulario('')
    setFormulario(nuevo)
  }

  async function guardar(cuerpo) {
    setGuardando(true)
    setErrorFormulario('')
    try {
      if (formulario.asesoria) {
        await editarAsesoria(formulario.asesoria.id, cuerpo)
        mostrarToast('Asesoría actualizada')
      } else {
        await crearAsesoria(cuerpo)
        mostrarToast('Asesoría creada')
      }
      setFormulario(null)
      cargar()
    } catch (err) {
      setErrorFormulario(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  function alVerInscritos(asesoria) {
    setSeleccionada(asesoria)
    setCargandoInscritos(true)
    verInscritos(asesoria.id)
      .then(setInscritos)
      .catch(() => setInscritos([]))
      .finally(() => setCargandoInscritos(false))
  }

  async function confirmarCancelacion() {
    const id = porCancelar.id
    setPorCancelar(null)
    setErrorAccion('')
    try {
      await cancelarAsesoria(id)
      mostrarToast('Asesoría cancelada')
      cargar()
      if (seleccionada?.id === id) setSeleccionada(null)
    } catch (err) {
      setErrorAccion(mensajeError(err, 'No se pudo cancelar'))
    }
  }

  const columnas = [
    { clave: 'materiaNombre', titulo: 'Materia' },
        {
      clave: 'fecha',
      titulo: 'Fecha y hora',
      render: (fila) => (
        <span className="panel-profesor__fecha">{formatearFechaHora(`${fila.fecha}T${fila.hora}`)}</span>
      ),
    },
    { clave: 'lugar', titulo: 'Lugar' },
    { clave: 'cupo', titulo: 'Cupo', render: (fila) => `${fila.inscritos}/${fila.cupoMax}` },
    { clave: 'estado', titulo: 'Estado' },
    {
      clave: 'acciones',
      titulo: '',
      render: (fila) => (
        <div className="admin__acciones">
          <Boton variante="secundario" onClick={() => alVerInscritos(fila)}>Ver inscritos</Boton>
          {fila.estado === 'ACTIVA' && (
            <>
              <Boton variante="secundario" onClick={() => abrirFormulario({ asesoria: fila })}>Editar</Boton>
              <Boton variante="secundario" onClick={() => setPorCancelar(fila)}>Cancelar</Boton>
            </>
          )}
        </div>
      ),
    },
  ]

  return (
    <section>
      <div className="admin__encabezado">
        <h1>Mis asesorías (profesor)</h1>
        <Boton onClick={() => abrirFormulario({})}>Crear asesoría</Boton>
      </div>

      {errorAccion && <AvisoError mensaje={errorAccion} />}

      {formulario && (
        <FormularioAsesoria
          key={formulario.asesoria?.id ?? 'nueva'}
          materias={materias}
          inicial={formulario.asesoria}
          onGuardar={guardar}
          onCancelar={() => setFormulario(null)}
          guardando={guardando}
          errorGeneral={errorFormulario}
        />
      )}

      {cargando && <Spinner mensaje="Cargando tus asesorías..." />}
      {!cargando && error && <AvisoError onReintentar={cargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio={<EstadoVacio icono="🗓️" titulo="Todavía no has creado ninguna asesoría" mensaje="Usa el botón Crear asesoría para publicar la primera." />}
        />
      )}
      
      {seleccionada && (
        <section className="inscritos">
          <header className="inscritos__encabezado">
            <div>
              <h2 className="inscritos__titulo">Inscritos en {seleccionada.materiaNombre}</h2>
              <p className="inscritos__detalle">
                {formatearFechaHora(`${seleccionada.fecha}T${seleccionada.hora}`)} · {seleccionada.lugar}
              </p>
            </div>
            <div className="inscritos__acciones">
              <span className="insignia insignia--neutra">
                {seleccionada.inscritos} de {seleccionada.cupoMax} cupos
              </span>
              <Boton variante="secundario" onClick={() => setSeleccionada(null)}>Cerrar</Boton>
            </div>
          </header>

          {cargandoInscritos ? (
            <Spinner mensaje="Cargando inscritos..." />
          ) : inscritos.length === 0 ? (
            <EstadoVacio icono="👥" titulo="Nadie se ha inscrito todavía" mensaje="Cuando un alumno se inscriba, aparecerá aquí." />
          ) : (
            <ul className="inscritos__lista">
              {inscritos.map((i) => (
                <li key={i.id} className="inscritos__item">
                  <Avatar nombre={i.alumnoNombre} />
                  <div className="inscritos__datos">
                    <span className="inscritos__nombre">{i.alumnoNombre}</span>
                    <span className="inscritos__correo">{i.alumnoCorreo}</span>
                  </div>
                  {i.inscritoEn && (
                    <span className="inscritos__fecha">Se inscribió el {formatearFechaHora(i.inscritoEn)}</span>
                  )}
                </li>
              ))}
            </ul>
          )}
        </section>
      )}

      <Modal
        abierto={porCancelar !== null}
        titulo="Cancelar asesoría"
        mensaje={porCancelar ? `¿Seguro que quieres cancelar la asesoría de ${porCancelar.materiaNombre} del ${porCancelar.fecha}? Se avisará por SMS a quienes estén inscritos.` : ''}
        onConfirmar={confirmarCancelacion}
        onCancelar={() => setPorCancelar(null)}
      />
    </section>
  )
}