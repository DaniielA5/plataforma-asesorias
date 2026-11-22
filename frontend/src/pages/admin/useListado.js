import { useCallback, useEffect, useState } from 'react'

export function useListado(cargar, filtros) {
  const [pagina, setPagina] = useState(null)
  const [page, setPage] = useState(0)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(false)
  const clave = JSON.stringify(filtros)

  const recargar = useCallback(() => {
    setCargando(true)
    setError(false)
    cargar({ ...JSON.parse(clave), page })
      .then(setPagina)
      .catch(() => setError(true))
      .finally(() => setCargando(false))
  }, [cargar, clave, page])

  useEffect(() => {
    recargar()
  }, [recargar])

  return { pagina, setPage, cargando, error, recargar }
}