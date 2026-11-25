import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const backend = env.VITE_BACKEND_URL || 'http://localhost:8080'

  return {
    plugins: [react()],
    server: {
          // Google lo pide para probar su botón en http://localhost.
      headers: { 'Referrer-Policy': 'no-referrer-when-downgrade' },
      proxy: {
        '/api': backend,
      },
    },
  }
})