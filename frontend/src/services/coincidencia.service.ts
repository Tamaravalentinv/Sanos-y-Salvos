import api from './api.client'
import { Coincidencia } from '@/types'

export const coincidenciaService = {
  getCoincidendasRecientes: async (limit: number = 10): Promise<Coincidencia[]> => {
    const response = await api.get<Coincidencia[]>('/coincidencias/recientes', {
      params: { limit },
    })
    return response.data
  },

  getCoincidendasPorReporte: async (reporteId: string): Promise<Coincidencia[]> => {
    const response = await api.get<Coincidencia[]>(`/coincidencias/reporte/${reporteId}`)
    return response.data
  },

  confirmarCoincidencia: async (coincidenciaId: string): Promise<Coincidencia> => {
    const response = await api.put<Coincidencia>(
      `/coincidencias/${coincidenciaId}/confirmar`,
      {}
    )
    return response.data
  },

  rechazarCoincidencia: async (coincidenciaId: string): Promise<void> => {
    await api.put(`/coincidencias/${coincidenciaId}/rechazar`, {})
  },

  buscarCoincidencias: async (reporteId: string): Promise<Coincidencia[]> => {
    const response = await api.post<Coincidencia[]>(
      `/coincidencias/buscar/${reporteId}`,
      {}
    )
    return response.data
  },

  getEstadisticas: async (): Promise<{
    totalCoincidencias: number
    coincidenciasConfirmadas: number
    tasaExito: number
  }> => {
    const response = await api.get('/coincidencias/estadisticas')
    return response.data
  },
}
