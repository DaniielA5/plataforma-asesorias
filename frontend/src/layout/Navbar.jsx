import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Avatar from '../components/Avatar'
import Boton from '../components/Boton'
import logo from '../assets/logo.svg'
import './Navbar.css'

const ENLACES = [
  { ruta: '/asesorias', texto: 'Catálogo', roles: ['ALUMNO', 'PROFESOR', 'ADMIN'] },
  { ruta: '/mis-asesorias', texto: 'Mis asesorías', roles: ['ALUMNO'] },
  { ruta: '/panel-profesor', texto: 'Mi panel', roles: ['PROFESOR'] },
  { ruta: '/admin', texto: 'Administración', roles: ['ADMIN'] },
]

function Navbar() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  if (!usuario) return null

  const enlaces = ENLACES.filter((enlace) => enlace.roles.includes(usuario.rol))

  async function cerrarSesion() {
    await logout()
    navigate('/login', { replace: true })
  }

  return (
    <header className="navbar">
      <Link to="/" className="navbar__marca">
        <img src={logo} alt="AyudITO" />
      </Link>

      <nav className="navbar__enlaces" aria-label="Principal">
        {enlaces.map((enlace) => (
          <NavLink
            key={enlace.ruta}
            to={enlace.ruta}
            className={({ isActive }) =>
              isActive ? 'navbar__enlace navbar__enlace--activo' : 'navbar__enlace'
            }
          >
            {enlace.texto}
          </NavLink>
        ))}
      </nav>

      <div className="navbar__usuario">
        <Link to="/perfil" className="navbar__perfil" title="Ir a mi perfil">
          <Avatar nombre={usuario.nombre} />
          <span className="navbar__datos">
            <span className="navbar__nombre">{usuario.nombre}</span>
            <span className="navbar__correo">{usuario.correo}</span>
          </span>
        </Link>
        <Boton variante="secundario" onClick={cerrarSesion}>
          Cerrar sesión
        </Boton>
      </div>
    </header>
  )
}

export default Navbar