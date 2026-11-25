import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Registro from './pages/Registro'
import Recuperar from './pages/Recuperar'
import Restablecer from './pages/Restablecer'
import RutaProtegida from './components/RutaProtegida'
import CatalogoAsesorias from './pages/CatalogoAsesorias'
import MisAsesorias from './pages/MisAsesorias'
import PanelProfesor from './pages/PanelProfesor'
import Layout from './layout/Layout'
import AuthLayout from './layout/AuthLayout'
import AdminLayout from './pages/admin/AdminLayout'
import UsuariosAdmin from './pages/admin/UsuariosAdmin'
import MateriasAdmin from './pages/admin/MateriasAdmin'
import NotificacionesAdmin from './pages/admin/NotificacionesAdmin'
import Perfil from './pages/perfil/Perfil'
import Inicio from './pages/Inicio'

function App() {
  return (
    <Routes>
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/registro" element={<Registro />} />
        <Route path="/recuperar" element={<Recuperar />} />
        <Route path="/restablecer" element={<Restablecer />} />
      </Route>

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
              <AdminLayout />
            </RutaProtegida>
          }
        >
          <Route index element={<Navigate to="usuarios" replace />} />
          <Route path="usuarios" element={<UsuariosAdmin />} />
          <Route path="materias" element={<MateriasAdmin />} />
          <Route path="notificaciones" element={<NotificacionesAdmin />} />
        </Route>
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
