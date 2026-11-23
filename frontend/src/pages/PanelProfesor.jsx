import { useEffect, useState } from 'react'
import { misAsesoriasProfesor, verInscritos, cancelarAsesoria } from '../api/asesorias'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Modal from '../components/Modal'
import EstadoVacio from '../components/EstadoVacio'
import { useToast } from '../context/ToastContext'

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

  useEffect(() => {
    cargar()
  }, [page])

  function cargar() {
    setCargando(true)
    setError(false)
    misAsesoriasProfesor(page)
      .then(setPagina)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }

  function alVerInscritos(asesoria) {
    setSeleccionada(asesoria)
    setCargandoInscritos(true)
    verInscritos(asesoria.id)
      .then(setInscritos)
      .finally(() => setCargandoInscritos(false))
  }

  async function confirmarCancelacion() {
    const id = porCancelar.id
    setPorCancelar(null)
    try {
      await cancelarAsesoria(id)
      mostrarToast('Asesoría cancelada')
      cargar()
      if (seleccionada?.id === id) setSeleccionada(null)
    } catch (err) {
      alert(err.response?.data?.mensaje || 'No se pudo cancelar')
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
        <>
          <Boton variante="secundario" onClick={() => alVerInscritos(fila)}>Ver inscritos</Boton>
          {' '}
          {fila.estado === 'ACTIVA' && (
            <Boton variante="secundario" onClick={() => setPorCancelar(fila)}>Cancelar</Boton>
          )}
        </>
      ),
    },
  ]

  return (
    <section>
      <h1>Mis asesorías (profesor)</h1>

      {cargando && <Spinner mensaje="Cargando tus asesorías..." />}
      {!cargando && error && <AvisoError onReintentar={cargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio={<EstadoVacio icono="🗓️" titulo="Todavía no has creado ninguna asesoría" mensaje="Cuando publiques tu primera asesoría, aparecerá aquí." />}
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
        mensaje={porCancelar ? `¿Seguro que quieres cancelar la asesoría de ${porCancelar.materiaNombre} del ${porCancelar.fecha}? Se avisará por WhatsApp a quienes estén inscritos.` : ''}
        onConfirmar={confirmarCancelacion}
        onCancelar={() => setPorCancelar(null)}
      />
    </section>
  )
}