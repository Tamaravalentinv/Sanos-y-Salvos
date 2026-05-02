import api from './api.client'
import { HotspotIncidencia, ZonaIncidencia } from '@/types'

export const geolocalizacionService = {
  getHotspotsIncidencia: async (ciudad?: string): Promise<HotspotIncidencia[]> => {
    const response = await api.get<HotspotIncidencia[]>('/geolocalizacion/hotspots', {
      params: ciudad ? { ciudad } : undefined,
    })
    return response.data
  },

  getZonasIncidencia: async (ciudad?: string): Promise<ZonaIncidencia[]> => {
    const response = await api.get<ZonaIncidencia[]>('/geolocalizacion/zonas', {
      params: ciudad ? { ciudad } : undefined,
    })
    return response.data
  },

  getAnalisisUbicacion: async (
    latitud: number,
    longitud: number
  ): Promise<{
    hotspot: HotspotIncidencia | null
    reportesCercanos: number
    riesgo: 'BAJO' | 'MEDIO' | 'ALTO'
  }> => {
    const response = await api.get('/geolocalizacion/analisis', {
      params: { latitud, longitud },
    })
    return response.data
  },

  rastrearUbicacion: async (reporteId: string, latitud: number, longitud: number) => {
    const response = await api.post('/geolocalizacion/rastrear', {
      reporteId,
      latitud,
      longitud,
    })
    return response.data
  },

  getCiudadesConReportes: async (): Promise<string[]> => {
    const response = await api.get<string[]>('/geolocalizacion/ciudades')
    return response.data
  },
}
