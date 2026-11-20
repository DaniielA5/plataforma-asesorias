import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Registro from './pages/Registro'
import Recuperar from './pages/Recuperar'
import Restablecer from './pages/Restablecer'
import RutaProtegida from './components/RutaProtegida'
import CatalogoAsesorias from './pages/CatalogoAsesorias'
import MisAsesorias from './pages/MisAsesorias'
import PanelProfesor from './pages/PanelProfesor'

function Inicio() {
  return <h1>Plataforma de Asesorías</h1>
}

function PanelAdmin() {
  return <h1>Panel de administración</h1>
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<Registro />} />
      <Route path="/recuperar" element={<Recuperar />} />
      <Route path="/restablecer" element={<Restablecer />} />
      <Route
        path="/"
        element={
          <RutaProtegida>
            <Inicio />
          </RutaProtegida>
        }
      />
      <Route
        path="/admin"
        element={
          <RutaProtegida rolesPermitidos={['ADMIN']}>
            <PanelAdmin />
          </RutaProtegida>
        }
      />
      <Route
        path="/asesorias"
        element={
          <RutaProtegida>
            <CatalogoAsesorias />
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
    </Routes>
  )
}

export default App