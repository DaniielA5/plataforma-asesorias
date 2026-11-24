import { useState } from 'react'
import { iniciales } from './iniciales'
import './Avatar.css'



const NOMBRES_MASCULINOS_EN_A = ['luca', 'joshua', 'garcia', 'borja', 'elias', 'matias', 'tobias', 'jonas']

function numeroDesde(texto) {
  let suma = 0
  for (const letra of texto) suma = (suma * 31 + letra.charCodeAt(0)) % 100
  return suma
}

function urlAvatar(nombre) {
  const limpio = (nombre || 'usuario').trim().toLowerCase()
  const primero = limpio.split(/\s+/)[0]
  const esMujer = primero.endsWith('a') && !NOMBRES_MASCULINOS_EN_A.includes(primero)
  const carpeta = esMujer ? 'women' : 'men'
  return `https://randomuser.me/api/portraits/${carpeta}/${numeroDesde(limpio)}.jpg`
}

function Avatar({ nombre, tamano = 40, src }) {
  const [fallo, setFallo] = useState(false)

  if (!fallo) {
    return (
      <img
        className="avatar"
        src={src || urlAvatar(nombre)}
        alt=""
        aria-hidden="true"
        width={tamano}
        height={tamano}
        style={{ width: tamano, height: tamano, objectFit: 'cover' }}
        onError={() => setFallo(true)}
      />
    )
  }

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