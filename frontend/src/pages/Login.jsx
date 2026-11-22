import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import FormularioLogin from './auth/FormularioLogin'
import logo from '../assets/logo.svg'
import './Login.css'

export default function Login() {
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  async function enviar({ correo, password }) {
    setError('')
    setEnviando(true)
    try {
      await login(correo, password)
      navigate('/')
    } catch {
      setError('Correo o contraseña incorrectos')
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="login-pagina">
      <div className="login-pagina__marketing">
        <div className="login-pagina__circulo login-pagina__circulo--acento" />
        <div className="login-pagina__circulo login-pagina__circulo--verde" />

        <div className="login-pagina__marca">AyudITO</div>
        <div>
          <span className="login-pagina__etiqueta">Plataforma de asesorías académicas</span>
          <h1 className="login-pagina__titulo">
            Aprende con <em>quien ya pasó</em> por donde tú andas.
          </h1>
          <p className="login-pagina__descripcion">
            Reserva asesorías con profesores y compañeros avanzados. Horarios reales, cupos claros
            y cero filas afuera del cubículo.
          </p>

          <div className="login-pagina__stats">
            <div>
              <div className="login-pagina__stat-numero">120+</div>
              <div className="login-pagina__stat-etiqueta">asesorías este semestre</div>
            </div>
            <div>
              <div className="login-pagina__stat-numero">34</div>
              <div className="login-pagina__stat-etiqueta">profesores y asesores</div>
            </div>
            <div>
              <div className="login-pagina__stat-numero">4.8</div>
              <div className="login-pagina__stat-etiqueta">calificación promedio</div>
            </div>
          </div>
        </div>

        <p className="login-pagina__pie">© 2026 AyudITO</p>
      </div>

      <div className="login-pagina__formulario">
        <FormularioLogin onSubmit={enviar} enviando={enviando} errorGeneral={error} />
      </div>
    </div>
  )
}