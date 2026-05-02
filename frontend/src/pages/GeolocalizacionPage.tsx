import React, { useEffect, useState } from 'react'
import Card from '@/components/Card'
import Badge from '@/components/Badge'
import { geolocalizacionService } from '@/services/geolocalizacion.service'
import {
  HotspotIncidencia,
  ZonaIncidencia,
} from '@/types'
import { calculateIntensityColor } from '@/utils/helpers'
import toast from 'react-hot-toast'

const GeolocalizacionPage: React.FC = () => {
  const [hotspots, setHotspots] = useState<HotspotIncidencia[]>([])
  const [zonas, setZonas] = useState<ZonaIncidencia[]>([])
  const [ciudades, setCiudades] = useState<string[]>([])
  const [selectedCiudad, setSelectedCiudad] = useState<string>('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadData()
  }, [selectedCiudad])

  const loadData = async () => {
    setLoading(true)
    try {
      const [hotspotsData, zonasData, ciudadesData] = await Promise.all([
        geolocalizacionService.getHotspotsIncidencia(selectedCiudad || undefined),
        geolocalizacionService.getZonasIncidencia(selectedCiudad || undefined),
        geolocalizacionService.getCiudadesConReportes(),
      ])
      setHotspots(hotspotsData)
      setZonas(zonasData)
      setCiudades(ciudadesData)
    } catch (error) {
      toast.error('Error al cargar datos de geolocalización')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6 h-full">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Mapa de Incidencias</h1>
        <p className="text-gray-600 mt-1">
          Visualiza las zonas con mayor concentración de reportes
        </p>
      </div>

      {/* Filter */}
      <Card>
        <div className="flex items-center gap-4">
          <label className="text-sm font-semibold text-gray-700">
            Filtrar por ciudad:
          </label>
          <select
            value={selectedCiudad}
            onChange={(e) => setSelectedCiudad(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
            <option value="">Todas las ciudades</option>
            {ciudades.map((ciudad) => (
              <option key={ciudad} value={ciudad}>
                {ciudad}
              </option>
            ))}
          </select>
        </div>
      </Card>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100vh-300px)]">
        {/* Map Placeholder */}
        <div className="lg:col-span-2 bg-gray-200 rounded-lg p-4 flex items-center justify-center">
          <div className="text-center">
            <p className="text-2xl mb-2">🗺️</p>
            <p className="text-gray-700 font-semibold">
              Mapa Interactivo
            </p>
            <p className="text-sm text-gray-600 mt-2">
              Integración con Google Maps o Leaflet
            </p>
            {hotspots.length > 0 && (
              <div className="mt-4 bg-white p-4 rounded inline-block">
                <p className="text-sm text-gray-700">
                  📍 {hotspots.length} hotspots encontrados
                </p>
              </div>
            )}
          </div>
        </div>

        {/* Sidebar with Data */}
        <div className="space-y-4 overflow-y-auto">
          {loading ? (
            <div className="flex items-center justify-center h-full">
              <div className="text-center">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto mb-2"></div>
                <p className="text-gray-600 text-sm">Cargando...</p>
              </div>
            </div>
          ) : (
            <>
              {/* Hotspots */}
              {hotspots.length > 0 && (
                <Card>
                  <h3 className="text-lg font-bold text-gray-900 mb-4">
                    Hotspots de Incidencia
                  </h3>
                  <div className="space-y-3">
                    {hotspots.map((hotspot) => (
                      <div
                        key={hotspot.id}
                        className="p-3 bg-gray-50 rounded-lg border border-gray-200"
                      >
                        <div className="flex items-center justify-between mb-2">
                          <p className="font-semibold text-gray-900">
                            {hotspot.ciudad}
                          </p>
                          <Badge
                            status={hotspot.intensidad}
                            className={calculateIntensityColor(hotspot.intensidad)}
                          >
                            {hotspot.intensidad}
                          </Badge>
                        </div>
                        <div className="text-sm text-gray-600 space-y-1">
                          <p>📍 {hotspot.latitud.toFixed(4)}, {hotspot.longitud.toFixed(4)}</p>
                          <p>📊 {hotspot.cantidadReportes} reportes</p>
                          {hotspot.especiesComunes.length > 0 && (
                            <p>🐾 {hotspot.especiesComunes.join(', ')}</p>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </Card>
              )}

              {/* Zones */}
              {zonas.length > 0 && (
                <Card>
                  <h3 className="text-lg font-bold text-gray-900 mb-4">
                    Zonas de Riesgo
                  </h3>
                  <div className="space-y-3">
                    {zonas.map((zona) => (
                      <div
                        key={zona.id}
                        className="p-3 bg-gray-50 rounded-lg border border-gray-200"
                      >
                        <p className="font-semibold text-gray-900 mb-2">
                          {zona.nombre}
                        </p>
                        <div className="text-sm text-gray-600 space-y-1">
                          <p>📍 {zona.latitud.toFixed(4)}, {zona.longitud.toFixed(4)}</p>
                          <p>📏 Radio: {zona.radio}km</p>
                          <p>⚠️ {zona.cantidadReportesActivos} reportes activos</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </Card>
              )}

              {hotspots.length === 0 && zonas.length === 0 && (
                <Card>
                  <div className="text-center py-6">
                    <p className="text-gray-500">
                      No hay datos de geolocalización disponibles
                    </p>
                  </div>
                </Card>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}

export default GeolocalizacionPage
