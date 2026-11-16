import './Boton.css'

function Boton({ children, onClick, tipo = 'button', variante = 'primario', deshabilitado = false }) {
  return (
    <button
      type={tipo}
      className={`boton boton--${variante}`}
      onClick={onClick}
      disabled={deshabilitado}
    >
      {children}
    </button>
  )
}

export default Boton