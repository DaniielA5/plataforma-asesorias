import { useEffect, useState } from 'react'
import { misAsesoriasProfesor, verInscritos, cancelarAsesoria, crearAsesoria, editarAsesoria } from '../api/asesorias'
import { listarMaterias } from '../api/materias'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Modal from '../components/Modal'
import EstadoVacio from '../components/EstadoVacio'
import FormularioAsesoria from './FormularioAsesoria'
import { useToast } from '../context/ToastContext'
import { mensajeError } from '../utils/errores'
import './admin/Admin.css'

export default function PanelProfesor() {
  const { mostrarToast } = useToast()
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
    listarMaterias().then(setMaterias).catch(() => {})
  }, [])

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
    { clave: 'fecha', titulo: 'Fecha' },
    { clave: 'hora', titulo: 'Hora' },
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
        <div>
          <h2>Inscritos en {seleccionada.materiaNombre} — {seleccionada.fecha}</h2>
          {cargandoInscritos ? (
            <Spinner mensaje="Cargando inscritos..." />
          ) : (
            <ul>
              {inscritos.length === 0 && <li>Nadie se ha inscrito todavía.</li>}
              {inscritos.map((i) => (
                <li key={i.id}>{i.alumnoNombre} — {i.alumnoCorreo}</li>
              ))}
            </ul>
          )}
        </div>
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