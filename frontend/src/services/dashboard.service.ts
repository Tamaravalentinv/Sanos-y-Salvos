import { EstadisticasGlobales, Reporte } from '@/types'
import { reporteService } from './reporte.service'

export interface ActividadReciente {
  fecha: string
  reportes: number
}

export interface CiudadResumen {
  ciudad: string
  total: number
}

function buildStats(reportes: Reporte[]): EstadisticasGlobales {
  const reportesActivos = reportes.filter((r) => r.estado === 'ACTIVO').length
  const reportesResueltos = reportes.filter((r) => r.estado === 'RESUELTO').length

  return {
    totalReportes: reportes.length,
    reportesActivos,
    reportesResueltos,
    tasaResolucion: reportes.length ? reportesResueltos / reportes.length : 0,
    coincidenciasDetectadas: 0,
    usuariosActivos: new Set(reportes.map((r) => r.usuarioId)).size,
  }
}

export const dashboardService = {
  getEstadisticasGlobales: async (): Promise<EstadisticasGlobales> => {
    const { content } = await reporteService.getAllReportes({ size: 500 })
    return buildStats(content)
  },

  getActividadReciente: async (dias = 7): Promise<ActividadReciente[]> => {
    const { content } = await reporteService.getAllReportes({ size: 500 })
    const now = new Date()
    const buckets = Array.from({ length: dias }, (_, index) => {
      const date = new Date(now)
      date.setDate(now.getDate() - (dias - index - 1))
      return { fecha: date.toISOString().slice(0, 10), reportes: 0 }
    })

    content.forEach((reporte) => {
      const fecha = reporte.fechaReporte.slice(0, 10)
      const bucket = buckets.find((item) => item.fecha === fecha)
      if (bucket) bucket.reportes += 1
    })

    return buckets
  },

  getTopCiudades: async (limit = 5): Promise<CiudadResumen[]> => {
    const { content } = await reporteService.getAllReportes({ size: 500 })
    const counts = content.reduce<Record<string, number>>((acc, reporte) => {
      const ciudad = reporte.ubicacion.ciudad || 'Sin ciudad'
      acc[ciudad] = (acc[ciudad] ?? 0) + 1
      return acc
    }, {})

    return Object.entries(counts)
      .map(([ciudad, total]) => ({ ciudad, total }))
      .sort((a, b) => b.total - a.total)
      .slice(0, limit)
  },

  getEstadisticasUsuario: async (usuarioId: string): Promise<EstadisticasGlobales> => {
    const reportes = await reporteService.getReportesByUsuario(usuarioId)
    return buildStats(reportes)
  },
}
