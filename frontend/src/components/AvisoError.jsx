import Boton from './Boton'
import './AvisoError.css'

function AvisoError({ mensaje = 'No pudimos conectar con el servidor. Revisa tu conexión e inténtalo de nuevo.', onReintentar }) {
  return (
    <div className="aviso-error" role="alert">
      <p className="aviso-error__mensaje">{mensaje}</p>
      {onReintentar && (
        <Boton variante="secundario" onClick={onReintentar}>
          Reintentar
        </Boton>
      )}
    </div>
  )
}

export default AvisoError