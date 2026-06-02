import api from './api.client'
import { Coincidencia } from '@/types'

interface BffCoincidenciasResponse {
  totalCoincidencias?: number
  coincidencias?: Coincidencia[]
  grupos?: Array<{ coincidencias?: Coincidencia[] }>
}

function flattenCoincidencias(data: BffCoincidenciasResponse): Coincidencia[] {
  if (Array.isArray(data.coincidencias)) return data.coincidencias
  if (Array.isArray(data.grupos)) {
    return data.grupos.flatMap((grupo) => grupo.coincidencias ?? [])
  }
  return []
}

export const coincidenciaService = {
  getCoincidenciasRecientes: async (limit = 10, userId = '1'): Promise<Coincidencia[]> => {
    const response = await api.get<BffCoincidenciasResponse>('/bff/coincidencias', {
      params: { userId },
    })
    return flattenCoincidencias(response.data).slice(0, limit)
  },

  getCoincidenciasPorReporte: async (reporteId: string, userId = '1'): Promise<Coincidencia[]> => {
    const coincidencias = await coincidenciaService.getCoincidenciasRecientes(100, userId)
    return coincidencias.filter(
      (coincidencia) =>
        coincidencia.reportePerdido?.id === reporteId ||
        coincidencia.reporteEncontrado?.id === reporteId
    )
  },

  getEstadisticas: async (userId = '1'): Promise<{
    totalCoincidencias: number
    coincidenciasConfirmadas: number
    tasaExito: number
  }> => {
    const response = await api.get<BffCoincidenciasResponse>('/bff/coincidencias', {
      params: { userId },
    })
    const coincidencias = flattenCoincidencias(response.data)
    const totalCoincidencias = response.data.totalCoincidencias ?? coincidencias.length
    const coincidenciasConfirmadas = coincidencias.filter((c) => c.estado === 'CONFIRMADA').length

    return {
      totalCoincidencias,
      coincidenciasConfirmadas,
      tasaExito: totalCoincidencias ? coincidenciasConfirmadas / totalCoincidencias : 0,
    }
  },
}
