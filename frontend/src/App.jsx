import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import RutaProtegida from './components/RutaProtegida'
import CatalogoAsesorias from './pages/CatalogoAsesorias'
import MisAsesorias from './pages/MisAsesorias'
import PanelProfesor from './pages/PanelProfesor'
import FormularioRegistro from './pages/auth/FormularioRegistro'
import FormularioRecuperar from './pages/auth/FormularioRecuperar'
import Layout from './layout/Layout'

function Inicio() {
  return <h1>Plataforma de Asesorías</h1>
}

function PanelAdmin() {
  return <h1>Panel de administración</h1>
}

// TODO: la pantalla real de perfil se hace en la tarea 22
function Perfil() {
  return <h1>Mi perfil</h1>
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      {/* TODO: conectar onSubmit con POST /api/auth/registro (lo hace Daniel) */}
      <Route path="/registro" element={<FormularioRegistro onSubmit={() => {}} />} />
      {/* TODO: conectar onSubmit con el endpoint de recuperar contraseña (lo hace Daniel) */}
      <Route path="/recuperar" element={<FormularioRecuperar onSubmit={() => {}} />} />

      <Route
        element={
          <RutaProtegida>
            <Layout />
          </RutaProtegida>
        }
      >
        <Route path="/" element={<Inicio />} />
        <Route path="/perfil" element={<Perfil />} />
        <Route path="/asesorias" element={<CatalogoAsesorias />} />
        <Route
          path="/admin"
          element={
            <RutaProtegida rolesPermitidos={['ADMIN']}>
              <PanelAdmin />
            </RutaProtegida>
          }
        />
        <Route
          path="/mis-asesorias"
          element={
            <RutaProtegida rolesPermitidos={['ALUMNO']}>
              <MisAsesorias />
            </RutaProtegida>
          }
        />
        <Route
          path="/panel-profesor"
          element={
            <RutaProtegida rolesPermitidos={['PROFESOR']}>
              <PanelProfesor />
            </RutaProtegida>
          }
        />
      </Route>
    </Routes>
  )
}

export default App