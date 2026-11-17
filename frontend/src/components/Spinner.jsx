import './Spinner.css'

function Spinner({ mensaje = 'Cargando...' }) {
  return (
    <div className="spinner" role="status" aria-live="polite">
      <span className="spinner__circulo" />
      <span className="spinner__texto">{mensaje}</span>
    </div>
  )
}

export default Spinner