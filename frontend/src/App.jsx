import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import RutaProtegida from './components/RutaProtegida'
import CatalogoAsesorias from './pages/CatalogoAsesorias'
import MisAsesorias from './pages/MisAsesorias'
import PanelProfesor from './pages/PanelProfesor'
import FormularioRegistro from './pages/auth/FormularioRegistro'
import FormularioRecuperar from './pages/auth/FormularioRecuperar'

function Inicio() {
  return <h1>Plataforma de Asesorías</h1>
}

function PanelAdmin() {
  return <h1>Panel de administración</h1>
}

function App() {
  return (
    <Routes>

       {/* TODO: conectar onSubmit con POST /api/auth/registro*/}
      <Route path="/registro" element={<FormularioRegistro onSubmit={() => {}} />} />
        {/* TODO: conectar onSubmit con el endpoint de recuperar contraseña*/}
      <Route path="/recuperar" element={<FormularioRecuperar onSubmit={() => {}} />} />


      <Route path="/login" element={<Login />} />
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