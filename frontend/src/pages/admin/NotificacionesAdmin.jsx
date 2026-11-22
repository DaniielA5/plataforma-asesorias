import { useState } from 'react'
import Tabla from '../../components/Tabla'
import Select from '../../components/Select'
import Spinner from '../../components/Spinner'
import AvisoError from '../../components/AvisoError'
import { listarNotificaciones } from './adminApi'
import { useListado } from './useListado'
import { formatearFechaHora } from '../../utils/fechas'
import './Admin.css'

const CANALES = [
  { valor: 'EMAIL', texto: 'Correo' },
  { valor: 'SMS', texto: 'SMS' },
  { valor: 'WHATSAPP', texto: 'WhatsApp' },
]

const ESTADOS = [
  { valor: 'ENVIADA', texto: 'Enviada' },
  { valor: 'FALLIDA', texto: 'Fallida' },
]

export default function NotificacionesAdmin() {
  const [canal, setCanal] = useState('')
  const [estado, setEstado] = useState('')
  const { pagina, setPage, cargando, error, recargar } = useListado(listarNotificaciones, { canal, estado })

  const columnas = [
    { clave: 'enviadaEn', titulo: 'Fecha', render: (n) => formatearFechaHora(n.enviadaEn) },
    { clave: 'canal', titulo: 'Canal' },
    { clave: 'destino', titulo: 'Destino' },
    { clave: 'asunto', titulo: 'Mensaje' },
    {
      clave: 'estado',
      titulo: 'Estado',
      render: (n) => (
        <span className={n.estado === 'ENVIADA' ? 'insignia insignia--ok' : 'insignia insignia--error'}>
          {n.estado}
        </span>
      ),
    },
    { clave: 'error', titulo: 'Motivo', render: (n) => n.error || '—' },
  ]

  return (
    <div>
      <div className="admin__encabezado">
        <h2>Registro de notificaciones</h2>
      </div>

      <div className="admin__barra">
        <Select
          id="filtro-canal"
          etiqueta="Canal"
          valor={canal}
          onChange={(e) => { setCanal(e.target.value); setPage(0) }}
          opcionVacia="Todos"
          opciones={CANALES}
        />
        <Select
          id="filtro-estado"
          etiqueta="Estado"
          valor={estado}
          onChange={(e) => { setEstado(e.target.value); setPage(0) }}
          opcionVacia="Todos"
          opciones={ESTADOS}
        />
      </div>

      {cargando && <Spinner mensaje="Cargando notificaciones..." />}
      {!cargando && error && <AvisoError onReintentar={recargar} />}
      {!cargando && !error && pagina && (
        <Tabla
          columnas={columnas}
          filas={pagina.content}
          page={pagina.page}
          totalPages={pagina.totalPages}
          onPageChange={setPage}
          mensajeVacio="Todavía no hay notificaciones."
        />
      )}
    </div>
  )
}