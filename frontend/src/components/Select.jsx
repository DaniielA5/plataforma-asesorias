import './Select.css'

function Select({ id, etiqueta, valor, onChange, opciones, opcionVacia }) {
  return (
    <div className="select-campo">
      {etiqueta && (
        <label htmlFor={id} className="select-campo__etiqueta">
          {etiqueta}
        </label>
      )}
      <select id={id} className="select-campo__select" value={valor} onChange={onChange}>
        {opcionVacia && <option value="">{opcionVacia}</option>}
        {opciones.map((opcion) => (
          <option key={opcion.valor} value={opcion.valor}>
            {opcion.texto}
          </option>
        ))}
      </select>
    </div>
  )
}

export default Select