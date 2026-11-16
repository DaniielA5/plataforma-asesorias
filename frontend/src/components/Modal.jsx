import { useEffect } from 'react'
import Boton from './Boton'
import './Modal.css'

function Modal({ abierto, titulo, mensaje, onConfirmar, onCancelar }) {
  useEffect(() => {
    if (!abierto) return

    const alPulsarTecla = (evento) => {
      if (evento.key === 'Escape') onCancelar()
    }

    document.addEventListener('keydown', alPulsarTecla)
    return () => document.removeEventListener('keydown', alPulsarTecla)
  }, [abierto, onCancelar])

  if (!abierto) return null

  return (
    <div className="modal__fondo" onClick={onCancelar}>
      <div
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-titulo"
        onClick={(evento) => evento.stopPropagation()}
      >
        <h2 id="modal-titulo" className="modal__titulo">{titulo}</h2>
        <p className="modal__mensaje">{mensaje}</p>
        <div className="modal__acciones">
          <Boton variante="secundario" onClick={onCancelar}>Cancelar</Boton>
          <Boton onClick={onConfirmar}>Confirmar</Boton>
        </div>
      </div>
    </div>
  )
}

export default Modal