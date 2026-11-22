import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Registro from './pages/Registro'
import Recuperar from './pages/Recuperar'
import Restablecer from './pages/Restablecer'
import RutaProtegida from './components/RutaProtegida'
import CatalogoAsesorias from './pages/CatalogoAsesorias'
import MisAsesorias from './pages/MisAsesorias'
import PanelProfesor from './pages/PanelProfesor'
import Layout from './layout/Layout'

import Inicio from './pages/Inicio'

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
      <Route path="/registro" element={<Registro />} />
      <Route path="/recuperar" element={<Recuperar />} />
      <Route path="/restablecer" element={<Restablecer />} />

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