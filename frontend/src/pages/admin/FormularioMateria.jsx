import { useState } from 'react'
import Input from '../../components/Input'
import Boton from '../../components/Boton'
import AvisoError from '../../components/AvisoError'
import './Admin.css'

function validarCodigo(valor) {
  const texto = valor.trim()
  if (!texto) return 'El código es obligatorio'
  if (texto.length > 20) return 'El código no puede pasar de 20 caracteres'
  return ''
}

function validarNombreMateria(valor) {
  const texto = valor.trim()
  if (!texto) return 'El nombre es obligatorio'
  if (texto.length > 120) return 'El nombre no puede pasar de 120 caracteres'
  return ''
}

export default function FormularioMateria({ inicial, onGuardar, onCancelar, guardando = false, errorGeneral = '' }) {
  const [datos, setDatos] = useState({ codigo: inicial?.codigo ?? '', nombre: inicial?.nombre ?? '' })
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  const errores = {
    codigo: validarCodigo(datos.codigo),
    nombre: validarNombreMateria(datos.nombre),
  }

  const cambiar = (campo) => (e) => {
    setDatos((actual) => ({ ...actual, [campo]: e.target.value }))
    setTocados((actual) => ({ ...actual, [campo]: true }))
  }
  const tocar = (campo) => () => setTocados((actual) => ({ ...actual, [campo]: true }))
  const mostrar = (campo) => (tocados[campo] || intento ? errores[campo] : '')

  function enviar(e) {
    e.preventDefault()
    setIntento(true)
    if (errores.codigo || errores.nombre) return
    onGuardar({ codigo: datos.codigo.trim(), nombre: datos.nombre.trim() })
  }

  return (
    <form className="admin__panel" onSubmit={enviar} noValidate>
      <h3>{inicial ? 'Editar materia' : 'Nueva materia'}</h3>
      {errorGeneral && <AvisoError mensaje={errorGeneral} />}
      <Input id="m-codigo" etiqueta="Código" valor={datos.codigo} onChange={cambiar('codigo')}
        onBlur={tocar('codigo')} placeholder="MAT-105" error={mostrar('codigo')} />
      <Input id="m-nombre" etiqueta="Nombre" valor={datos.nombre} onChange={cambiar('nombre')}
        onBlur={tocar('nombre')} error={mostrar('nombre')} />
      <div className="admin__acciones">
        <Boton tipo="submit" deshabilitado={guardando}>{guardando ? 'Guardando...' : 'Guardar'}</Boton>
        <Boton variante="secundario" onClick={onCancelar}>Cancelar</Boton>
      </div>
    </form>
  )
}