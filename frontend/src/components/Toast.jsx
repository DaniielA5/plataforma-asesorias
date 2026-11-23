import './Toast.css'

export default function Toast({ mensaje, tipo = 'exito', onCerrar }) {
  return (
    <div className={`toast toast--${tipo}`} role="status">
      <span>{mensaje}</span>
      <button type="button" className="toast__cerrar" onClick={onCerrar} aria-label="Cerrar aviso">
        ×
      </button>
    </div>
  )
}