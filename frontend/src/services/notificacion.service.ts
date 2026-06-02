import api from './api.client'
import { Notificacion } from '@/types'

interface BackendNotificacion {
  id: number
  usuarioId: number
  tipo: string
  asunto?: string
  titulo?: string
  contenido?: string
  mensaje?: string
  estado?: string
  eventoId?: number
  fechaCreacion?: string
  canalEnvio?: string
}

function mapNotificacion(notificacion: BackendNotificacion): Notificacion {
  const leida = notificacion.estado === 'LEIDA' || notificacion.estado === 'ENVIADA'

  return {
    id: String(notificacion.id),
    titulo: notificacion.titulo ?? notificacion.asunto ?? 'Notificacion',
    mensaje: notificacion.mensaje ?? notificacion.contenido ?? '',
    tipo: notificacion.tipo === 'COINCIDENCIA' || notificacion.tipo === 'RESOLUCION' || notificacion.tipo === 'ALERTA'
      ? notificacion.tipo
      : 'INFORMACION',
    estado: leida ? 'LEIDA' : 'NO_LEIDA',
    usuarioId: String(notificacion.usuarioId),
    relatedReporteId: notificacion.eventoId ? String(notificacion.eventoId) : undefined,
    fechaCreacion: notificacion.fechaCreacion ?? new Date().toISOString(),
    canalEnvio: notificacion.canalEnvio === 'EMAIL' || notificacion.canalEnvio === 'SMS' || notificacion.canalEnvio === 'PUSH'
      ? notificacion.canalEnvio
      : 'INTERNO',
  }
}

export const notificacionService = {
  getNotificaciones: async (usuarioId: string): Promise<Notificacion[]> => {
    const response = await api.get<BackendNotificacion[]>(`/notificaciones/user/${usuarioId}`)
    return response.data.map(mapNotificacion)
  },

  getNotificacionesNoLeidas: async (usuarioId: string): Promise<Notificacion[]> => {
    const response = await api.get<BackendNotificacion[]>(`/notificaciones/user/${usuarioId}/no-leidas`)
    return response.data.map(mapNotificacion)
  },

  marcarComoLeida: async (notificacionId: string): Promise<void> => {
    await api.patch(`/notificaciones/${notificacionId}/leer`)
  },

  deleteNotificacion: async (notificacionId: string): Promise<void> => {
    await api.delete(`/notificaciones/${notificacionId}`)
  },
}
