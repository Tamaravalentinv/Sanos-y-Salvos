import api from './api.client'
import { ZonaIncidencia } from '@/types'

// Backend ZonaIncidencia shape (from ms-geolocalizacion /hotzones)
interface BackendZona {
  id: number
  nombre: string
  latitudCentro: number
  longitudCentro: number
  radioKm: number
  numIncidencias: number
  numPerdidas: number
  numEncontradas: number
  nivelRiesgo: string
  esActiva: boolean
}

function mapZona(z: BackendZona): ZonaIncidencia {
  return {
    id: String(z.id),
    nombre: z.nombre,
    latitud: z.latitudCentro,
    longitud: z.longitudCentro,
    radio: z.radioKm,
    cantidadReportesActivos: z.numIncidencias,
  }
}

export const geolocalizacionService = {
  getZonasIncidencia: async (): Promise<ZonaIncidencia[]> => {
    try {
      const response = await api.get<BackendZona[]>('/geolocalizacion/hotzones/activas')
      return response.data.map(mapZona)
    } catch {
      return []
    }
  },

  getUbicacionesCercanas: async (
    latitud: number,
    longitud: number,
    radioKm = 5
  ) => {
    const response = await api.get('/geolocalizacion/cercania', {
      params: { latitud, longitud, radioKm },
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
}
