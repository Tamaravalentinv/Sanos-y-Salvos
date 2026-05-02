// Auth & User Types
export interface User {
  id: string
  nombre: string
  apellido: string
  email: string
  telefono?: string
  tipoUsuario: 'CIUDADANO' | 'CLINICA' | 'REFUGIO' | 'MUNICIPALIDAD'
  organizacion?: string
  fotoPerfil?: string
}

export interface AuthResponse {
  token: string
  user: User
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  nombre: string
  apellido: string
  email: string
  password: string
  telefono?: string
  tipoUsuario: string
  organizacion?: string
}

// Reporte Types
export interface Reporte {
  id: string
  titulo: string
  descripcion: string
  tipo: 'PERDIDO' | 'ENCONTRADO'
  estado: 'ACTIVO' | 'RESUELTO' | 'CANCELADO'
  mascota: Mascota
  ubicacion: UbicacionReporte
  usuarioId: string
  imagenes?: string[]
  fechaReporte: string
  fechaResolucion?: string
  detallesResolucion?: string
}

export interface Mascota {
  id: string
  nombre: string
  especie: 'PERRO' | 'GATO' | 'OTRO'
  raza: string
  color: string
  tamaño: 'PEQUEÑO' | 'MEDIANO' | 'GRANDE'
  edad?: number
  señas_particulares?: string
  fotografia?: string
}

export interface UbicacionReporte {
  id: string
  latitud: number
  longitud: number
  direccion: string
  ciudad: string
  pais: string
}

// Coincidencia Types
export interface Coincidencia {
  id: string
  reportePerdido: Reporte
  reporteEncontrado: Reporte
  scoreMatching: number
  factoresCoincidencia: FactorCoincidencia[]
  estado: 'SUGERENCIA' | 'CONFIRMADA' | 'RECHAZADA'
  fechaDeteccion: string
}

export interface FactorCoincidencia {
  factor: 'RAZA' | 'COLOR' | 'TAMAÑO' | 'UBICACION' | 'FECHA' | 'ESPECIE'
  score: number
  detalle: string
}

// Geolocalización Types
export interface HotspotIncidencia {
  id: string
  latitud: number
  longitud: number
  ciudad: string
  intensidad: 'BAJA' | 'MEDIA' | 'ALTA'
  cantidadReportes: number
  especiesComunes: string[]
  fechaActualizacion: string
}

export interface ZonaIncidencia {
  id: string
  nombre: string
  latitud: number
  longitud: number
  radio: number
  cantidadReportesActivos: number
}

// Notificación Types
export interface Notificacion {
  id: string
  titulo: string
  mensaje: string
  tipo: 'COINCIDENCIA' | 'RESOLUCION' | 'INFORMACION' | 'ALERTA'
  estado: 'LEIDA' | 'NO_LEIDA'
  usuarioId: string
  relatedReporteId?: string
  fechaCreacion: string
  canalEnvio: 'EMAIL' | 'SMS' | 'PUSH' | 'INTERNO'
}

export interface PreferenciasNotificacion {
  emailEnabled: boolean
  smsEnabled: boolean
  pushEnabled: boolean
  internoEnabled: boolean
  notificarCoincidencias: boolean
  notificarResoluciones: boolean
}

// Organización Types
export interface Organizacion {
  id: string
  nombre: string
  tipo: 'CLINICA' | 'REFUGIO' | 'MUNICIPALIDAD'
  telefono?: string
  ubicacion?: UbicacionReporte
  sitioWeb?: string
  contactoRepresentante?: string
  email: string
}

// Dashboard Stats
export interface EstadisticasGlobales {
  totalReportes: number
  reportesActivos: number
  reportesResueltos: number
  tasaResolucion: number
  coincidenciasDetectadas: number
  usuariosActivos: number
}

// Error Response
export interface ApiError {
  message: string
  code: string
  details?: Record<string, unknown>
}
