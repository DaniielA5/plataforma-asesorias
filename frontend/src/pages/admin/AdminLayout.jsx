import { NavLink, Outlet } from 'react-router-dom'
import './Admin.css'

const SECCIONES = [
  { ruta: '/admin/usuarios', texto: 'Usuarios' },
  { ruta: '/admin/materias', texto: 'Materias' },
  { ruta: '/admin/notificaciones', texto: 'Notificaciones' },
]

export default function AdminLayout() {
  return (
    <section>
      <h1>Administración</h1>
      <nav className="admin__tabs" aria-label="Secciones de administración">
        {SECCIONES.map((seccion) => (
          <NavLink
            key={seccion.ruta}
            to={seccion.ruta}
            className={({ isActive }) => (isActive ? 'admin__tab admin__tab--activa' : 'admin__tab')}
          >
            {seccion.texto}
          </NavLink>
        ))}
      </nav>
      <Outlet />
    </section>
  )
}