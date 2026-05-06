import React, { useEffect, useState } from 'react'
import { FiMapPin, FiAlertTriangle, FiFilter } from 'react-icons/fi'
import Card from '@/components/Card'
import Badge from '@/components/Badge'
import Select from '@/components/Select'
import { geolocalizacionService } from '@/services/geolocalizacion.service'
import { HotspotIncidencia, ZonaIncidencia } from '@/types'
import toast from 'react-hot-toast'

const IntensityBar = ({ intensidad }: { intensidad: string }) => {
  const w = intensidad === 'ALTA' ? 'w-full' : intensidad === 'MEDIA' ? 'w-2/3' : 'w-1/3'
  const color = intensidad === 'ALTA' ? 'bg-red-500' : intensidad === 'MEDIA' ? 'bg-amber-500' : 'bg-emerald-500'
  return (
    <div className="w-full h-1.5 bg-slate-100 rounded-full overflow-hidden">
      <div className={`h-full ${w} ${color} rounded-full transition-all`} />
    </div>
  )
}

const GeolocalizacionPage: React.FC = () => {
  const [hotspots, setHotspots] = useState<HotspotIncidencia[]>([])
  const [zonas, setZonas] = useState<ZonaIncidencia[]>([])
  const [ciudades, setCiudades] = useState<string[]>([])
  const [selectedCiudad, setSelectedCiudad] = useState<string>('')
  const [loading, setLoading] = useState(true)

  useEffect(() => { loadData() }, [selectedCiudad])

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
    } catch {
      toast.error('Error al cargar datos de geolocalización')
    } finally {
      setLoading(false)
    }
  }

  const ciudadOptions = ciudades.map(c => ({ value: c, label: c }))

  return (
    <div className="space-y-5 fade-in">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Mapa de Incidencias</h1>
        <p className="text-slate-500 text-sm mt-0.5">Visualiza zonas con mayor concentración de reportes</p>
      </div>

      {/* Filter */}
      <Card padding="sm">
        <div className="flex items-center gap-3">
          <FiFilter size={16} className="text-slate-400 flex-shrink-0" />
          <div className="flex-1">
            <Select
              label=""
              value={selectedCiudad}
              onChange={e => setSelectedCiudad(e.target.value)}
              options={ciudadOptions}
              placeholder="Todas las ciudades"
            />
          </div>
        </div>
      </Card>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        {/* Map placeholder */}
        <div className="lg:col-span-2">
          <Card padding="none" className="h-[500px] overflow-hidden relative">
            {/* Decorative map background */}
            <div className="absolute inset-0 bg-gradient-to-br from-primary-50 via-sky-50 to-primary-100">
              {/* Grid lines */}
              <svg className="absolute inset-0 w-full h-full opacity-20" xmlns="http://www.w3.org/2000/svg">
                <defs>
                  <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                    <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#0ea5e9" strokeWidth="0.5"/>
                  </pattern>
                </defs>
                <rect width="100%" height="100%" fill="url(#grid)" />
              </svg>
            </div>

            {/* Hotspot dots */}
            {!loading && hotspots.map((h, i) => (
              <div
                key={h.id}
                className="absolute"
                style={{
                  left: `${15 + (i * 18) % 70}%`,
                  top: `${20 + (i * 23) % 60}%`,
                }}
              >
                <div className={`relative w-8 h-8 rounded-full flex items-center justify-center shadow-lg
                  ${h.intensidad === 'ALTA' ? 'bg-red-500' : h.intensidad === 'MEDIA' ? 'bg-amber-500' : 'bg-emerald-500'}`}>
                  <FiMapPin size={14} className="text-white" />
                  {h.intensidad === 'ALTA' && (
                    <span className="absolute -top-1 -right-1 w-3 h-3 bg-red-400 rounded-full animate-ping" />
                  )}
                </div>
                <div className="mt-1 bg-white rounded-lg px-2 py-0.5 shadow text-xs font-semibold text-slate-700 whitespace-nowrap">
                  {h.ciudad}
                </div>
              </div>
            ))}

            {/* Center content */}
            <div className="absolute inset-0 flex items-end justify-center pb-6 pointer-events-none">
              <div className="bg-white/90 backdrop-blur-sm rounded-2xl px-5 py-3 shadow-lg flex items-center gap-3">
                <span className="text-2xl">🗺️</span>
                <div>
                  <p className="font-semibold text-slate-800 text-sm">Mapa Interactivo</p>
                  <p className="text-xs text-slate-500">
                    {loading ? 'Cargando datos…' : `${hotspots.length} hotspots · ${zonas.length} zonas de riesgo`}
                  </p>
                </div>
              </div>
            </div>

            {/* Legend */}
            <div className="absolute top-4 right-4 bg-white/90 backdrop-blur-sm rounded-xl p-3 shadow text-xs space-y-1.5">
              {[
                { label: 'Alta intensidad', color: 'bg-red-500' },
                { label: 'Media intensidad', color: 'bg-amber-500' },
                { label: 'Baja intensidad', color: 'bg-emerald-500' },
              ].map(item => (
                <div key={item.label} className="flex items-center gap-2">
                  <div className={`w-3 h-3 rounded-full ${item.color}`} />
                  <span className="text-slate-600">{item.label}</span>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* Data sidebar */}
        <div className="space-y-4 overflow-y-auto max-h-[500px] pr-0.5">
          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map(i => (
                <Card key={i} className="animate-pulse">
                  <div className="h-20 bg-slate-100 rounded-xl" />
                </Card>
              ))}
            </div>
          ) : (
            <>
              {/* Hotspots */}
              {hotspots.length > 0 && (
                <Card>
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-7 h-7 bg-red-50 rounded-lg flex items-center justify-center">
                      <FiMapPin size={14} className="text-red-500" />
                    </div>
                    <h3 className="font-bold text-slate-800 text-sm">Hotspots</h3>
                  </div>
                  <div className="space-y-3">
                    {hotspots.map(h => (
                      <div key={h.id} className="p-3 bg-slate-50 rounded-xl">
                        <div className="flex items-center justify-between mb-1.5">
                          <p className="font-semibold text-slate-800 text-sm">{h.ciudad}</p>
                          <Badge status={h.intensidad}>{h.intensidad}</Badge>
                        </div>
                        <IntensityBar intensidad={h.intensidad} />
                        <div className="mt-2 text-xs text-slate-500 space-y-0.5">
                          <p>📊 {h.cantidadReportes} reportes</p>
                          {h.especiesComunes.length > 0 && <p>🐾 {h.especiesComunes.join(', ')}</p>}
                        </div>
                      </div>
                    ))}
                  </div>
                </Card>
              )}

              {/* Zones */}
              {zonas.length > 0 && (
                <Card>
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-7 h-7 bg-amber-50 rounded-lg flex items-center justify-center">
                      <FiAlertTriangle size={14} className="text-amber-500" />
                    </div>
                    <h3 className="font-bold text-slate-800 text-sm">Zonas de Riesgo</h3>
                  </div>
                  <div className="space-y-3">
                    {zonas.map(z => (
                      <div key={z.id} className="p-3 bg-slate-50 rounded-xl">
                        <p className="font-semibold text-slate-800 text-sm mb-1">{z.nombre}</p>
                        <div className="text-xs text-slate-500 space-y-0.5">
                          <p>📏 Radio: {z.radio} km</p>
                          <p>⚠️ {z.cantidadReportesActivos} reportes activos</p>
                          <p>📍 {z.latitud.toFixed(4)}, {z.longitud.toFixed(4)}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </Card>
              )}

              {hotspots.length === 0 && zonas.length === 0 && (
                <Card className="text-center py-12">
                  <div className="text-5xl mb-3">📍</div>
                  <p className="text-slate-500 font-medium text-sm">Sin datos disponibles</p>
                  <p className="text-slate-400 text-xs mt-1">Prueba con otra ciudad</p>
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
