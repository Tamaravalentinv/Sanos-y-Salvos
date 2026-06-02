// Configuración de la aplicación

export const APP_CONFIG = {
  name: 'Sanos y Salvos',
  version: '1.0.0',
  description: 'Plataforma de recuperación de mascotas perdidas',
  environment: import.meta.env.MODE,
} as const;

export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 30000,
  withCredentials: true,
} as const;

export const AUTH_CONFIG = {
  tokenKey: 'token',
  userKey: 'user',
} as const;

export const MAP_CONFIG = {
  defaultCenter: [40.4168, -3.7038], // Madrid, España
  defaultZoom: 13,
  maxZoom: 18,
  minZoom: 3,
} as const;
