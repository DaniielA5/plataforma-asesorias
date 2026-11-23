import { useEffect, useState } from 'react'
import { misInscripciones, cancelarInscripcion } from '../api/asesorias'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Modal from '../components/Modal'
import EstadoVacio from '../components/EstadoVacio'

export default function MisAsesorias() {
  const [inscripciones, setInscripciones] = useState([])
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(false)
  const [porCancelar, setPorCancelar] = useState(null)

  useEffect(() => {
    cargar()
  }, [])

  function cargar() {
    setCargando(true)
    setError(false)
    misInscripciones()
      .then(setInscripciones)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }

  async function confirmarCancelacion() {
    const asesoriaId = porCancelar.asesoria.id
    setPorCancelar(null)
    try {
      await cancelarInscripcion(asesoriaId)
      cargar()
    } catch (err) {
      alert(err.response?.data?.mensaje || 'No se pudo cancelar')
    }
  }

  const columnas = [
    { clave: 'materia', titulo: 'Materia', render: (fila) => fila.asesoria.materiaNombre },
    { clave: 'profesor', titulo: 'Profesor', render: (fila) => fila.asesoria.profesorNombre },
    { clave: 'fecha', titulo: 'Fecha', render: (fila) => fila.asesoria.fecha },
    { clave: 'hora', titulo: 'Hora', render: (fila) => fila.asesoria.hora },
    { clave: 'lugar', titulo: 'Lugar', render: (fila) => fila.asesoria.lugar },
    { clave: 'estado', titulo: 'Estado', render: (fila) => fila.asesoria.estado },
    {
      clave: 'accion',
      titulo: '',
      render: (fila) => (
        <Boton variante="secundario" onClick={() => setPorCancelar(fila)}>
          Cancelar
        </Boton>
      ),
    },
  ]

  return (
    <section>
      <h1>Mis asesorías</h1>

      {cargando && <Spinner mensaje="Cargando tus asesorías..." />}
      {!cargando && error && <AvisoError onReintentar={cargar} />}
      {!cargando && !error && (
        <Tabla
          columnas={columnas}
          filas={inscripciones}
          page={0}
          totalPages={1}
          onPageChange={() => {}}
          mensajeVacio={<EstadoVacio icono="📚" titulo="Todavía no te has inscrito a ninguna asesoría" textoAccion="Ver catálogo" rutaAccion="/asesorias" />}
        />
      )}

      <Modal
        abierto={porCancelar !== null}
        titulo="Cancelar inscripción"
        mensaje={porCancelar ? `¿Seguro que quieres cancelar tu inscripción a ${porCancelar.asesoria.materiaNombre}?` : ''}
        onConfirmar={confirmarCancelacion}
        onCancelar={() => setPorCancelar(null)}
      />
    </section>
  )
}