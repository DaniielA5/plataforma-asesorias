import { useEffect, useState } from 'react'
import { listarAsesorias, inscribirse } from '../api/asesorias'
import { listarMaterias } from '../api/materias'
import { useAuth } from '../context/AuthContext'
import Tabla from '../components/Tabla'
import Spinner from '../components/Spinner'
import AvisoError from '../components/AvisoError'
import Boton from '../components/Boton'
import Select from '../components/Select'
import EstadoVacio from '../components/EstadoVacio'

export default function CatalogoAsesorias() {
  const { usuario } = useAuth()
  const [materias, setMaterias] = useState([])
  const [materiaId, setMateriaId] = useState('')
  const [pagina, setPagina] = useState(null)
  const [page, setPage] = useState(0)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(false)
  const [inscribiendoId, setInscribiendoId] = useState(null)
    const [errorInscripcion, setErrorInscripcion] = useState('')

  useEffect(() => {
    listarMaterias().then(setMaterias).catch(() => {})
  }, [])

  useEffect(() => {
    cargar()
  }, [materiaId, page])

  function cargar() {
    setCargando(true)
    setError(false)
    listarAsesorias({ materiaId, page })
      .then(setPagina)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }

    async function alInscribirme(asesoriaId) {
    setInscribiendoId(asesoriaId)
    setErrorInscripcion('')
    try {
      await inscribirse(asesoriaId)
      cargar()
    } catch (err) {
      setErrorInscripcion(err.response?.data?.mensaje || 'No se pudo inscribir')
    } finally {
      setInscribiendoId(null)
    }
  }

  const columnas = [
    {
      clave: 'materiaNombre',
      titulo: 'Materia',
      render: (fila) => (
        <span className={`catalogo__materia catalogo__materia--${fila.materiaId % 2 === 0 ? 'gris' : 'amarillo'}`}>
          {fila.materiaNombre}
        </span>
      ),
    },
    { clave: 'profesorNombre', titulo: 'Profesor' },
    { clave: 'fecha', titulo: 'Fecha' },
    { clave: 'hora', titulo: 'Hora' },
    { clave: 'lugar', titulo: 'Lugar' },
    { clave: 'cupo', titulo: 'Cupo', render: (fila) => `${fila.inscritos}/${fila.cupoMax}` },
    {
      clave: 'accion',
      titulo: '',
      render: (fila) => {
        if (usuario?.rol !== 'ALUMNO') return null
        const llena = fila.inscritos >= fila.cupoMax
        return (
          <Boton
            variante="secundario"
            deshabilitado={llena || inscribiendoId === fila.id}
            onClick={() => alInscribirme(fila.id)}
          >
            {llena ? 'Sin cupo' : 'Inscribirme'}
          </Boton>
        )
      },
    },
  ]

  return (
    <section>
      <h1>Catálogo de asesorías</h1>
      {errorInscripcion && <AvisoError mensaje={errorInscripcion} />}

            <Select
        id="filtro-materia"
        etiqueta="Materia"
        valor={materiaId}
        onChange={(e) => {
          setPage(0)
          setMateriaId(e.target.value)
        }}
        opcionVacia="Todas"
        opciones={materias.map((m) => ({ valor: m.id, texto: m.nombre }))}
      />

      {cargando && <Spinner mensaje="Cargando asesorías..." />}
      {!cargando && error && <AvisoError onReintentar={cargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio={<EstadoVacio icono="📭" titulo="No hay asesorías con ese filtro" mensaje="Prueba con otra materia o quita el filtro." />}
        />
      )}
    </section>
  )
}