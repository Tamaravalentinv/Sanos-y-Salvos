import { create } from 'zustand'
import { Notificacion } from '@/types'

const STORAGE_KEY = 'sanos_notificaciones'

function loadFromStorage(): Notificacion[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

function saveToStorage(notificaciones: Notificacion[]): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(notificaciones))
}

interface NotificacionStore {
  notificaciones: Notificacion[]
  noLeidasCount: number
  isLoading: boolean
  error: string | null

  loadNotificaciones: () => Promise<void>
  loadNoLeidasCount: () => Promise<void>
  marcarComoLeida: (id: string) => Promise<void>
  marcarTodasComoLeidas: () => Promise<void>
  deleteNotificacion: (id: string) => Promise<void>
  addNotificacion: (notificacion: Notificacion) => void
  clearAll: () => Promise<void>
}

export const useNotificacionStore = create<NotificacionStore>((set, get) => ({
  notificaciones: loadFromStorage(),
  noLeidasCount: loadFromStorage().filter(n => n.estado === 'NO_LEIDA').length,
  isLoading: false,
  error: null,

  loadNotificaciones: async () => {
    const stored = loadFromStorage()
    set({ notificaciones: stored, isLoading: false })
  },

  loadNoLeidasCount: async () => {
    const count = get().notificaciones.filter(n => n.estado === 'NO_LEIDA').length
    set({ noLeidasCount: count })
  },

  marcarComoLeida: async (id: string) => {
    const notificaciones = get().notificaciones.map(n =>
      n.id === id ? { ...n, estado: 'LEIDA' as const } : n
    )
    saveToStorage(notificaciones)
    set({ notificaciones })
    get().loadNoLeidasCount()
  },

  marcarTodasComoLeidas: async () => {
    const notificaciones = get().notificaciones.map(n => ({ ...n, estado: 'LEIDA' as const }))
    saveToStorage(notificaciones)
    set({ notificaciones, noLeidasCount: 0 })
  },

  deleteNotificacion: async (id: string) => {
    const notificaciones = get().notificaciones.filter(n => n.id !== id)
    saveToStorage(notificaciones)
    set({ notificaciones })
    get().loadNoLeidasCount()
  },

  addNotificacion: (notificacion: Notificacion) => {
    const notificaciones = [notificacion, ...get().notificaciones]
    saveToStorage(notificaciones)
    set({ notificaciones })
    if (notificacion.estado === 'NO_LEIDA') {
      set({ noLeidasCount: get().noLeidasCount + 1 })
    }
  },

  clearAll: async () => {
    localStorage.removeItem(STORAGE_KEY)
    set({ notificaciones: [], noLeidasCount: 0 })
  },
}))
