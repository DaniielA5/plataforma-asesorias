import './Input.css'

function Input({ id, etiqueta, tipo = 'text', valor, onChange, onBlur, placeholder, error }) {
  const idError = `${id}-error`

  return (
    <div className="input-campo">
      {etiqueta && (
        <label htmlFor={id} className="input-campo__etiqueta">
          {etiqueta}
        </label>
      )}
      <input
        id={id}
        type={tipo}
        value={valor}
        onChange={onChange}
        onBlur={onBlur}
        placeholder={placeholder}
        className={`input-campo__input ${error ? 'input-campo__input--error' : ''}`}
        aria-invalid={error ? 'true' : 'false'}
        aria-describedby={error ? idError : undefined}
      />
      {error && (
        <p id={idError} className="input-campo__error" role="alert">
          {error}
        </p>
      )}
    </div>
  )
}

export default Input