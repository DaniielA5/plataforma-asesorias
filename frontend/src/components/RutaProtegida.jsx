import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function RutaProtegida({ rolesPermitidos, children }) {
    const { usuario, cargado } = useAuth()

    if (cargado) {
        return <p>Cargando...</p>
    }

    if(!usuario) {
        return <Navigate to="/login" replace />
    }

 if (rolesPermitidos && !rolesPermitidos.includes(usuario.rol)) {
    return <Navigate to="/" replace/>
 }

 return children
}