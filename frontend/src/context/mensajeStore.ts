import { create } from 'zustand'
import { Mensaje } from '@/types'

const STORAGE_KEY = 'sanos_mensajes'

function load(): Mensaje[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) : []
  } catch { return [] }
}

function save(msgs: Mensaje[]): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(msgs))
}

interface MensajeStore {
  mensajes: Mensaje[]
  enviarMensaje: (data: Omit<Mensaje, 'id' | 'leido' | 'fechaCreacion'>) => void
  marcarLeido: (id: string) => void
  getNoLeidosCount: (userId: string) => number
}

export const useMensajeStore = create<MensajeStore>((set, get) => ({
  mensajes: load(),

  enviarMensaje: (data) => {
    const msg: Mensaje = {
      ...data,
      id: `msg_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`,
      leido: false,
      fechaCreacion: new Date().toISOString(),
    }
    const updated = [msg, ...get().mensajes]
    save(updated)
    set({ mensajes: updated })
  },

  marcarLeido: (id) => {
    const updated = get().mensajes.map(m => m.id === id ? { ...m, leido: true } : m)
    save(updated)
    set({ mensajes: updated })
  },

  getNoLeidosCount: (userId) =>
    get().mensajes.filter(m => m.toUserId === userId && !m.leido).length,
}))
