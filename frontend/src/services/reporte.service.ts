import api from './api.client'
import { Reporte } from '@/types'

export const reporteService = {
  getAllReportes: async (filtros?: {
    tipo?: string
    estado?: string
    ciudad?: string
    page?: number
    size?: number
  }): Promise<{ content: Reporte[]; totalElements: number; totalPages: number }> => {
    const response = await api.get('/reportes', { params: filtros })
    return response.data
  },

  getReporteById: async (id: string): Promise<Reporte> => {
    const response = await api.get<Reporte>(`/reportes/${id}`)
    return response.data
  },

  createReporte: async (data: Omit<Reporte, 'id' | 'fechaReporte'>): Promise<Reporte> => {
    const response = await api.post<Reporte>('/reportes', data)
    return response.data
  },

  updateReporte: async (id: string, data: Partial<Reporte>): Promise<Reporte> => {
    const response = await api.put<Reporte>(`/reportes/${id}`, data)
    return response.data
  },

  deleteReporte: async (id: string): Promise<void> => {
    await api.delete(`/reportes/${id}`)
  },

  getReportesByUsuario: async (usuarioId: string): Promise<Reporte[]> => {
    const response = await api.get<Reporte[]>(`/reportes/usuario/${usuarioId}`)
    return response.data
  },

  resolverReporte: async (
    id: string,
    detallesResolucion: string
  ): Promise<Reporte> => {
    const response = await api.put<Reporte>(`/reportes/${id}/resolver`, {
      detallesResolucion,
    })
    return response.data
  },

  subirImagenReporte: async (reporteId: string, archivo: File): Promise<string> => {
    const formData = new FormData()
    formData.append('file', archivo)
    const response = await api.post<{ url: string }>(
      `/reportes/${reporteId}/imagen`,
      formData,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
      }
    )
    return response.data.url
  },

  buscarReportesCercanos: async (
    latitud: number,
    longitud: number,
    radiusKm: number
  ): Promise<Reporte[]> => {
    const response = await api.get<Reporte[]>('/reportes/cercanos', {
      params: { latitud, longitud, radiusKm },
    })
    return response.data
  },
}
