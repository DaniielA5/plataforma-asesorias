let promesa

// Carga el script de Google una sola vez, la primera vez que se necesita.
export function cargarGoogle() {
  if (window.google?.accounts?.id) return Promise.resolve(window.google)

  promesa ??= new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.src = 'https://accounts.google.com/gsi/client?hl=es'
    script.async = true
    script.onload = () => resolve(window.google)
    script.onerror = () => {
      promesa = undefined
      script.remove()
      reject(new Error('No se pudo cargar Google'))
    }
    document.head.appendChild(script)
  })
  return promesa
}