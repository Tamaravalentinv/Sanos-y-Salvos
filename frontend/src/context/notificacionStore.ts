import { create } from 'zustand'
import { Notificacion } from '@/types'
import { notificacionService } from '@/services/notificacion.service'

interface NotificacionStore {
  notificaciones: Notificacion[]
  noLeidasCount: number
  isLoading: boolean
  error: string | null
  
  // Actions
  loadNotificaciones: () => Promise<void>
  loadNoLeidasCount: () => Promise<void>
  marcarComoLeida: (id: string) => Promise<void>
  marcarTodasComoLeidas: () => Promise<void>
  deleteNotificacion: (id: string) => Promise<void>
  addNotificacion: (notificacion: Notificacion) => void
  clearAll: () => Promise<void>
}

export const useNotificacionStore = create<NotificacionStore>((set, get) => ({
  notificaciones: [],
  noLeidasCount: 0,
  isLoading: false,
  error: null,

  loadNotificaciones: async () => {
    set({ isLoading: true, error: null })
    try {
      const result = await notificacionService.getNotificaciones()
      set({
        notificaciones: result.content,
        isLoading: false,
      })
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Error al cargar notificaciones'
      set({ error: errorMessage, isLoading: false })
    }
  },

  loadNoLeidasCount: async () => {
    try {
      const count = await notificacionService.getNotificacionesNoLeidas()
      set({ noLeidasCount: count })
    } catch (error) {
      console.error('Error loading unread count:', error)
    }
  },

  marcarComoLeida: async (id: string) => {
    try {
      await notificacionService.marcarComoLeida(id)
      const notificaciones = get().notificaciones.map((n) =>
        n.id === id ? { ...n, estado: 'LEIDA' as const } : n
      )
      set({ notificaciones })
      get().loadNoLeidasCount()
    } catch (error) {
      console.error('Error marking notification as read:', error)
    }
  },

  marcarTodasComoLeidas: async () => {
    try {
      await notificacionService.marcarTodasComoLeidas()
      const notificaciones = get().notificaciones.map((n) => ({
        ...n,
        estado: 'LEIDA' as const,
      }))
      set({ notificaciones, noLeidasCount: 0 })
    } catch (error) {
      console.error('Error marking all as read:', error)
    }
  },

  deleteNotificacion: async (id: string) => {
    try {
      await notificacionService.deleteNotificacion(id)
      const notificaciones = get().notificaciones.filter((n) => n.id !== id)
      set({ notificaciones })
      get().loadNoLeidasCount()
    } catch (error) {
      console.error('Error deleting notification:', error)
    }
  },

  addNotificacion: (notificacion: Notificacion) => {
    const notificaciones = [notificacion, ...get().notificaciones]
    set({ notificaciones })
    if (notificacion.estado === 'NO_LEIDA') {
      set({ noLeidasCount: get().noLeidasCount + 1 })
    }
  },

  clearAll: async () => {
    try {
      await notificacionService.deleteTodasNotificaciones()
      set({ notificaciones: [], noLeidasCount: 0 })
    } catch (error) {
      console.error('Error clearing all notifications:', error)
    }
  },
}))
