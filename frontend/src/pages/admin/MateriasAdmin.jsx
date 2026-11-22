import { useState } from 'react'
import Tabla from '../../components/Tabla'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import Modal from '../../components/Modal'
import Spinner from '../../components/Spinner'
import AvisoError from '../../components/AvisoError'
import FormularioMateria from './FormularioMateria'
import { useListado } from './useListado'
import { crearMateria, editarMateria, eliminarMateria, listarMaterias } from './adminApi'
import { mensajeError } from '../../utils/errores'
import './Admin.css'

export default function MateriasAdmin() {
  const [busqueda, setBusqueda] = useState('')
  const [q, setQ] = useState('')
  const { pagina, setPage, cargando, error, recargar } = useListado(listarMaterias, { q })

  const [formulario, setFormulario] = useState(null)
  const [guardando, setGuardando] = useState(false)
  const [errorFormulario, setErrorFormulario] = useState('')
  const [aEliminar, setAEliminar] = useState(null)
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
      if (formulario.materia) {
        await editarMateria(formulario.materia.id, cuerpo)
        setAviso('Materia actualizada')
      } else {
        await crearMateria(cuerpo)
        setAviso('Materia creada')
      }
      setFormulario(null)
      recargar()
    } catch (err) {
      setErrorFormulario(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  async function confirmarEliminacion() {
    const materia = aEliminar
    setAEliminar(null)
    setAviso('')
    setErrorAccion('')
    try {
      await eliminarMateria(materia.id)
      setAviso('Materia eliminada')
      recargar()
    } catch (err) {
      setErrorAccion(mensajeError(err))
    }
  }

  const columnas = [
    { clave: 'codigo', titulo: 'Código' },
    { clave: 'nombre', titulo: 'Nombre' },
    {
      clave: 'acciones',
      titulo: '',
      render: (m) => (
        <div className="admin__acciones">
          <Boton variante="secundario" onClick={() => abrirFormulario({ materia: m })}>Editar</Boton>
          <Boton variante="secundario" onClick={() => setAEliminar(m)}>Eliminar</Boton>
        </div>
      ),
    },
  ]

  return (
    <div>
      <div className="admin__encabezado">
        <h2>Materias</h2>
        <Boton onClick={() => abrirFormulario({})}>Nueva materia</Boton>
      </div>

      {aviso && <p className="admin__aviso" role="status">{aviso}</p>}
      {errorAccion && <AvisoError mensaje={errorAccion} />}

      {formulario && (
        <FormularioMateria
          key={formulario.materia?.id ?? 'nueva'}
          inicial={formulario.materia}
          onGuardar={guardar}
          onCancelar={() => setFormulario(null)}
          guardando={guardando}
          errorGeneral={errorFormulario}
        />
      )}

      <form className="admin__barra" onSubmit={buscar}>
        <Input id="busqueda-materias" etiqueta="Buscar" valor={busqueda}
          onChange={(e) => setBusqueda(e.target.value)} placeholder="Nombre o código" />
        <Boton tipo="submit">Buscar</Boton>
      </form>

      {cargando && <Spinner mensaje="Cargando materias..." />}
      {!cargando && error && <AvisoError onReintentar={recargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio="No hay materias con ese filtro."
        />
      )}

      <Modal
        abierto={Boolean(aEliminar)}
        titulo="Eliminar materia"
        mensaje={`Vas a eliminar ${aEliminar?.nombre}. Esta acción no se puede deshacer, y no se puede eliminar si ya tiene asesorías.`}
        onConfirmar={confirmarEliminacion}
        onCancelar={() => setAEliminar(null)}
      />
    </div>
  )
}