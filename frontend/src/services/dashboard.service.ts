import api from './api.client'
import { EstadisticasGlobales } from '@/types'

export const dashboardService = {
  getEstadisticasGlobales: async (): Promise<EstadisticasGlobales> => {
    const response = await api.get<EstadisticasGlobales>('/dashboard/estadisticas')
    return response.data
  },

  getActividadReciente: async (dias: number = 7): Promise<any[]> => {
    const response = await api.get('/dashboard/actividad-reciente', {
      params: { dias },
    })
    return response.data
  },

  getTopCiudades: async (limit: number = 5): Promise<any[]> => {
    const response = await api.get('/dashboard/top-ciudades', {
      params: { limit },
    })
    return response.data
  },

  getEstadisticasUsuario: async (usuarioId: string): Promise<any> => {
    const response = await api.get(`/dashboard/usuario/${usuarioId}`)
    return response.data
  },

  exportarReportes: async (formato: 'PDF' | 'CSV' | 'EXCEL'): Promise<Blob> => {
    const response = await api.get(`/dashboard/exportar/${formato}`, {
      responseType: 'blob',
    })
    return response.data
  },
}
