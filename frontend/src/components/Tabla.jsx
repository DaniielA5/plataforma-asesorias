import Boton from './Boton'
import './Tabla.css'

function Tabla({ columnas, filas, page, totalPages, onPageChange, mensajeVacio = 'No hay resultados.' }) {
  return (
    <div className="tabla">
      <div className="tabla__contenedor">
        <table className="tabla__tabla">
          <thead>
            <tr>
              {columnas.map((columna) => (
                <th key={columna.clave}>{columna.titulo}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filas.length === 0 ? (
              <tr>
                <td colSpan={columnas.length} className="tabla__vacio">
                  {mensajeVacio}
                </td>
              </tr>
            ) : (
              filas.map((fila) => (
                <tr key={fila.id}>
                  {columnas.map((columna) => (
                    <td key={columna.clave}>
                      {columna.render ? columna.render(fila) : fila[columna.clave]}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <nav className="tabla__paginacion" aria-label="Paginación">
          <Boton
            variante="secundario"
            deshabilitado={page === 0}
            onClick={() => onPageChange(page - 1)}
          >
            Anterior
          </Boton>
          <span className="tabla__pagina">
            Página {page + 1} de {totalPages}
          </span>
          <Boton
            variante="secundario"
            deshabilitado={page >= totalPages - 1}
            onClick={() => onPageChange(page + 1)}
          >
            Siguiente
          </Boton>
        </nav>
      )}
    </div>
  )
}

export default Tabla