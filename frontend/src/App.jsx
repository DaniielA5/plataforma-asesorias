import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import RutaProtegida from './components/RutaProtegida'

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
    </Routes>
  )
}

export default App