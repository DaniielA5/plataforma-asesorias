import './ReglasPassword.css'

function ReglasPassword({ reglas, activa = false }) {
  return (
    <ul className="reglas-password" aria-live="polite">
      {reglas.map((regla) => {
        let estado = 'reglas-password__regla'
        if (activa) {
          estado += regla.cumple ? ' reglas-password__regla--ok' : ' reglas-password__regla--falta'
        }
        return (
          <li key={regla.id} className={estado}>
            <span aria-hidden="true">{regla.cumple ? '✓' : '•'}</span> {regla.texto}
          </li>
        )
      })}
    </ul>
  )
}

export default ReglasPassword