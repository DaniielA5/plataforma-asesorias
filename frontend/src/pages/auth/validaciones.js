export const REGLAS_PASSWORD = [
  { id: 'longitud', texto: 'Mínimo 8 caracteres', cumple: (p) => p.length >= 8 },
  { id: 'mayuscula', texto: 'Al menos una letra mayúscula', cumple: (p) => /[A-Z]/.test(p) },
  { id: 'numero', texto: 'Al menos un número', cumple: (p) => /[0-9]/.test(p) },
  { id: 'especial', texto: 'Al menos un carácter especial (por ejemplo ! @ #)', cumple: (p) => /[^A-Za-z0-9\s]/.test(p) },
]

export function evaluarReglasPassword(password) {
  return REGLAS_PASSWORD.map(({ id, texto, cumple }) => ({ id, texto, cumple: cumple(password) }))
}

export function passwordValida(password) {
  return REGLAS_PASSWORD.every((regla) => regla.cumple(password))
}

export function validarCorreo(correo) {
  const texto = correo.trim()
  if (!texto) return 'El correo es obligatorio'
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(texto)) return 'Escribe un correo válido, por ejemplo nombre@correo.com'
  return ''
}

export function validarNombre(nombre) {
  const texto = nombre.trim()
  if (!texto) return 'El nombre es obligatorio'
  if (texto.length < 3) return 'El nombre debe tener al menos 3 caracteres'
  if (texto.length > 120) return 'El nombre no puede pasar de 120 caracteres'
  return ''
}

export function validarTelefono(telefono) {
  const texto = telefono.trim()
  if (!texto) return 'El teléfono es obligatorio'
  if (!/^\+\d{10,15}$/.test(texto)) return 'Usa el formato internacional sin espacios, por ejemplo +529512345672'
  return ''
}

export function validarConfirmacion(password, confirmacion) {
  if (!confirmacion) return 'Confirma tu contraseña'
  if (password !== confirmacion) return 'Las contraseñas no coinciden'
  return ''
}