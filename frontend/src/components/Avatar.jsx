import { iniciales } from './iniciales'
import './Avatar.css'

function Avatar({ nombre, tamano = 40 }) {
  return (
    <span
      className="avatar"
      style={{ width: tamano, height: tamano, fontSize: tamano * 0.4 }}
      aria-hidden="true"
    >
      {iniciales(nombre)}
    </span>
  )
}

export default Avatar