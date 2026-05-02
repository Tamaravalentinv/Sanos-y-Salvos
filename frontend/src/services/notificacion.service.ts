import api from './api.client'
import { Notificacion, PreferenciasNotificacion } from '@/types'

export const notificacionService = {
  getNotificaciones: async (
    leidas?: boolean,
    page?: number,
    size?: number
  ): Promise<{ content: Notificacion[]; total: number }> => {
    const response = await api.get('/notificaciones', {
      params: { leidas, page, size },
    })
    return response.data
  },

  marcarComoLeida: async (notificacionId: string): Promise<Notificacion> => {
    const response = await api.put<Notificacion>(
      `/notificaciones/${notificacionId}/leida`,
      {}
    )
    return response.data
  },

  marcarTodasComoLeidas: async (): Promise<void> => {
    await api.put('/notificaciones/leer-todas', {})
  },

  deleteNotificacion: async (notificacionId: string): Promise<void> => {
    await api.delete(`/notificaciones/${notificacionId}`)
  },

  deleteTodasNotificaciones: async (): Promise<void> => {
    await api.delete('/notificaciones/limpiar-todas')
  },

  getPreferencias: async (): Promise<PreferenciasNotificacion> => {
    const response = await api.get<PreferenciasNotificacion>(
      '/notificaciones/preferencias'
    )
    return response.data
  },

  updatePreferencias: async (
    preferencias: Partial<PreferenciasNotificacion>
  ): Promise<PreferenciasNotificacion> => {
    const response = await api.put<PreferenciasNotificacion>(
      '/notificaciones/preferencias',
      preferencias
    )
    return response.data
  },

  getNotificacionesNoLeidas: async (): Promise<number> => {
    const response = await api.get<number>('/notificaciones/no-leidas-count')
    return response.data
  },
}
