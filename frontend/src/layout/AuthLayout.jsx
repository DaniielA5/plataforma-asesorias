import { Outlet } from 'react-router-dom'
import logo from '../assets/logo.svg'
import silueta from '../assets/silueta-estudiante.svg'
import './AuthLayout.css'

// Plantilla de las pantallas de acceso: la burbuja a la izquierda y el formulario a la derecha.
export default function AuthLayout() {
  return (
    <div className="auth">
      <aside className="auth__burbuja">
        <div className="auth__panel">
          <img className="auth__silueta" src={silueta} alt="" />

          <div className="auth__mensaje">
            <p className="auth__titular">
              Aprende con <em>quien ya pasó</em> por donde tú andas.
            </p>
            <p className="auth__descripcion">
              Reserva asesorías con profesores y compañeros avanzados. Horarios reales, cupos claros
              y cero filas afuera del cubículo.
            </p>
          </div>

          <div className="auth__chat">
            <p className="auth__globo auth__globo--pregunta">
              ¿Quién me explica integrales por partes?
            </p>
            <p className="auth__globo auth__globo--respuesta">
              Yo te ayudo. Aparta tu lugar para el martes.
            </p>
          </div>
        </div>

        <svg className="auth__cola" viewBox="0 0 140 48" aria-hidden="true">
          <path d="M0 0H140C92 6 46 24 10 45C3 49 0 46 0 39Z" />
        </svg>
      </aside>

      <main className="auth__contenido">
        <div className="auth__caja">
          <img className="auth__logo" src={logo} alt="AyudITO" />
          <Outlet />
        </div>
      </main>
    </div>
  )
}
