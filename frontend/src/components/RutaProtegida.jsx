import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { faltaTelefono } from "../utils/usuario";

export default function RutaProtegida({ rolesPermitidos, children }) {
    const { usuario, cargando } = useAuth()

    if (cargando) {
        return <p>Cargando...</p>
    }

    if (!usuario) {
        return <Navigate to="/login" replace />
    }
    if (faltaTelefono(usuario)) {
        return <Navigate to="/completar-telefono" replace />
    }

    if (rolesPermitidos && !rolesPermitidos.includes(usuario.rol)) {
        return <Navigate to="/" replace />
    }

    return children
}