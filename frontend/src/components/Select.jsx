import './Select.css'

function Select({ id, etiqueta, valor, onChange, opciones, opcionVacia, error }) {
  const idError = `${id}-error`

  return (
    <div className="select-campo">
      {etiqueta && (
        <label htmlFor={id} className="select-campo__etiqueta">
          {etiqueta}
        </label>
      )}
      <select
        id={id}
        className={`select-campo__select ${error ? 'select-campo__select--error' : ''}`}
        value={valor}
        onChange={onChange}
        aria-invalid={error ? 'true' : 'false'}
        aria-describedby={error ? idError : undefined}
      >
        {opcionVacia && <option value="">{opcionVacia}</option>}
        {opciones.map((opcion) => (
          <option key={opcion.valor} value={opcion.valor}>
            {opcion.texto}
          </option>
        ))}
      </select>
      {error && (
        <p id={idError} className="select-campo__error" role="alert">
          {error}
        </p>
      )}
    </div>
  )
}

export default Select