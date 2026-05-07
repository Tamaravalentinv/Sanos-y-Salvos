// Constantes de la aplicación

export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  DASHBOARD: '/dashboard',
  REPORTES: '/reportes',
  CREAR_REPORTE: '/reportes/crear',
  REPORTE_DETALLE: '/reportes/:id',
  COINCIDENCIAS: '/coincidencias',
  MENSAJES: '/mensajes',
  NOTIFICACIONES: '/notificaciones',
  GEOLOCALIZACION: '/geolocalizacion',
  PERFIL: '/perfil',
} as const;

export const STATUS = {
  ACTIVO: 'activo',
  INACTIVO: 'inactivo',
  PENDIENTE: 'pendiente',
  RESUELTO: 'resuelto',
} as const;

export const TIMEOUT = {
  SHORT: 3000,
  MEDIUM: 5000,
  LONG: 10000,
} as const;
