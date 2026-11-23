import { Link } from 'react-router-dom'
import Boton from './Boton'
import './EstadoVacio.css'

export default function EstadoVacio({ icono = '🔍', titulo, mensaje, textoAccion, rutaAccion }) {
  return (
    <div className="estado-vacio">
      <span className="estado-vacio__icono" aria-hidden="true">{icono}</span>
      <p className="estado-vacio__titulo">{titulo}</p>
      {mensaje && <p className="estado-vacio__mensaje">{mensaje}</p>}
      {textoAccion && rutaAccion && (
        <Link to={rutaAccion}>
          <Boton variante="secundario">{textoAccion}</Boton>
        </Link>
      )}
    </div>
  )
}