import { createContext, useCallback, useContext, useRef, useState } from 'react'
import Toast from '../components/Toast'

const ToastContext = createContext(null)

export function ToastProvider({ children }) {
  const [toast, setToast] = useState(null)
  const timeoutRef = useRef(null)

  const mostrarToast = useCallback((mensaje, tipo = 'exito') => {
    clearTimeout(timeoutRef.current)
    setToast({ mensaje, tipo })
    timeoutRef.current = setTimeout(() => setToast(null), 3500)
  }, [])

  return (
    <ToastContext.Provider value={{ mostrarToast }}>
      {children}
      {toast && <Toast mensaje={toast.mensaje} tipo={toast.tipo} onCerrar={() => setToast(null)} />}
    </ToastContext.Provider>
  )
}

export function useToast() {
  return useContext(ToastContext)
}