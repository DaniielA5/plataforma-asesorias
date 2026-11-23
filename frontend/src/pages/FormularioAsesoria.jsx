import { useState } from 'react'
import Input from '../components/Input'
import Select from '../components/Select'
import Boton from '../components/Boton'
import AvisoError from '../components/AvisoError'
import './admin/Admin.css'

function hoy() {
  const d = new Date()
  const mes = String(d.getMonth() + 1).padStart(2, '0')
  const dia = String(d.getDate()).padStart(2, '0')
  return `${d.getFullYear()}-${mes}-${dia}`
}

function validarFecha(valor, original) {
  if (!valor) return 'La fecha es obligatoria'
  if (valor !== original && valor < hoy()) return 'La fecha no puede ser anterior a hoy'
  return ''
}

function validarLugar(valor) {
  const texto = valor.trim()
  if (!texto) return 'El lugar es obligatorio'
  if (texto.length > 120) return 'El lugar no puede pasar de 120 caracteres'
  return ''
}

function validarCupo(valor, minimo) {
  if (!valor) return 'El cupo es obligatorio'
  const n = Number(valor)
  if (!Number.isInteger(n) || n < 1) return 'El cupo debe ser un número entero mayor a 0'
  if (n > 100) return 'El cupo no puede pasar de 100'
  if (minimo && n < minimo) return `Ya hay ${minimo} inscritos, el cupo no puede ser menor`
  return ''
}

export default function FormularioAsesoria({ materias, inicial, onGuardar, onCancelar, guardando = false, errorGeneral = '' }) {
  const editando = Boolean(inicial)
  const [datos, setDatos] = useState({
    materiaId: inicial ? String(inicial.materiaId) : '',
    fecha: inicial?.fecha ?? '',
    hora: inicial?.hora ? inicial.hora.slice(0, 5) : '',
    lugar: inicial?.lugar ?? '',
    cupoMax: inicial ? String(inicial.cupoMax) : '',
    notas: inicial?.notas ?? '',
  })
  const [tocados, setTocados] = useState({})
  const [intento, setIntento] = useState(false)

  const errores = {
    materiaId: datos.materiaId ? '' : 'Elige una materia',
    fecha: validarFecha(datos.fecha, inicial?.fecha),
    hora: datos.hora ? '' : 'La hora es obligatoria',
    lugar: validarLugar(datos.lugar),
    cupoMax: validarCupo(datos.cupoMax, inicial?.inscritos),
    notas: datos.notas.length > 500 ? 'Las notas no pueden pasar de 500 caracteres' : '',
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
    if (Object.values(errores).some(Boolean)) return
    onGuardar({
      materiaId: Number(datos.materiaId),
      fecha: datos.fecha,
      hora: datos.hora,
      lugar: datos.lugar.trim(),
      cupoMax: Number(datos.cupoMax),
      notas: datos.notas.trim() || null,
    })
  }

  return (
    <form className="admin__panel" onSubmit={enviar} noValidate>
      <h3>{editando ? 'Editar asesoría' : 'Nueva asesoría'}</h3>
      {errorGeneral && <AvisoError mensaje={errorGeneral} />}

      <Select
        id="a-materia"
        etiqueta="Materia"
        valor={datos.materiaId}
        onChange={cambiar('materiaId')}
        opcionVacia="Elige una materia"
        opciones={materias.map((m) => ({ valor: m.id, texto: m.nombre }))}
        error={mostrar('materiaId')}
      />
      <Input id="a-fecha" etiqueta="Fecha" tipo="date" valor={datos.fecha}
        onChange={cambiar('fecha')} onBlur={tocar('fecha')} error={mostrar('fecha')} />
      <Input id="a-hora" etiqueta="Hora" tipo="time" valor={datos.hora}
        onChange={cambiar('hora')} onBlur={tocar('hora')} error={mostrar('hora')} />
      <Input id="a-lugar" etiqueta="Lugar" valor={datos.lugar}
        onChange={cambiar('lugar')} onBlur={tocar('lugar')} placeholder="Aula B-204" error={mostrar('lugar')} />
      <Input id="a-cupo" etiqueta="Cupo máximo" tipo="number" valor={datos.cupoMax}
        onChange={cambiar('cupoMax')} onBlur={tocar('cupoMax')} error={mostrar('cupoMax')} />
      <Input id="a-notas" etiqueta="Notas (opcional)" valor={datos.notas}
        onChange={cambiar('notas')} onBlur={tocar('notas')} error={mostrar('notas')} />

      <div className="admin__acciones">
        <Boton tipo="submit" deshabilitado={guardando}>{guardando ? 'Guardando...' : 'Guardar'}</Boton>
        <Boton variante="secundario" onClick={onCancelar}>Cancelar</Boton>
      </div>
    </form>
  )
}