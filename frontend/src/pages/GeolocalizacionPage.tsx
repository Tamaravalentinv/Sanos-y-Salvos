import React, { useEffect, useState } from 'react'
import { MapContainer, TileLayer, Marker, CircleMarker, Popup, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { useNavigate } from 'react-router-dom'
import { FiMapPin, FiAlertTriangle, FiPlus } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import { geolocalizacionService } from '@/services/geolocalizacion.service'
import { reporteService } from '@/services/reporte.service'
import { ZonaIncidencia, Reporte } from '@/types'

interface LeafletDefaultIconPrototype extends L.Icon.Default {
  _getIconUrl?: unknown
}

delete (L.Icon.Default.prototype as LeafletDefaultIconPrototype)._getIconUrl
L.Icon.Default.mergeOptions({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})

const makeIcon = (color: string) =>
  L.divIcon({
    html: `<div style="background:${color};width:22px;height:22px;border-radius:50%;border:3px solid white;box-shadow:0 2px 6px rgba(0,0,0,0.35)"></div>`,
    className: '',
    iconSize: [22, 22],
    iconAnchor: [11, 11],
    popupAnchor: [0, -14],
  })

const LOST_ICON = makeIcon('#ef4444')
const FOUND_ICON = makeIcon('#22c55e')

interface ClickPos { lat: number; lng: number }

const MapClickCapture: React.FC<{ onMapClick: (lat: number, lng: number) => void }> = ({ onMapClick }) => {
  useMapEvents({ click: (e) => onMapClick(e.latlng.lat, e.latlng.lng) })
  return null
}

const GeolocalizacionPage: React.FC = () => {
  const navigate = useNavigate()
  const [zonas, setZonas] = useState<ZonaIncidencia[]>([])
  const [reportes, setReportes] = useState<Reporte[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [clickPos, setClickPos] = useState<ClickPos | null>(null)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    setError(null)
    try {
      const [zonasData, reportesData] = await Promise.allSettled([
        geolocalizacionService.getZonasIncidencia(),
        reporteService.getAllReportes({ size: 200 }),
      ])

      if (zonasData.status === 'fulfilled') setZonas(zonasData.value)
      if (reportesData.status === 'fulfilled') {
        setReportes(
          reportesData.value.content.filter(
            (r) =>
              r.estado === 'ACTIVO' &&
              r.ubicacion &&
              (r.ubicacion.latitud !== 0 || r.ubicacion.longitud !== 0)
          )
        )
      } else {
        setError('No se pudieron cargar los reportes del mapa')
      }
      if (zonasData.status === 'rejected' && reportesData.status === 'rejected') {
        setError('No se pudo cargar la informacion del mapa')
      }
    } finally {
      setLoading(false)
    }
  }

  const lost = reportes.filter((r) => r.tipo === 'PERDIDO')
  const found = reportes.filter((r) => r.tipo === 'ENCONTRADO')

  return (
    <div className="space-y-5 fade-in">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Mapa de Mascotas</h1>
          <p className="text-slate-500 text-sm mt-0.5">
            Haz clic en el mapa para crear un reporte ·{' '}
            <span className="text-red-500 font-medium">● Perdidas</span>{' '}
            <span className="text-emerald-500 font-medium">● Encontradas</span>
          </p>
        </div>
        <Button variant="accent" size="sm" onClick={() => navigate('/reportes/crear')}>
          <FiPlus size={14} /> Nuevo Reporte
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        {/* Leaflet Map */}
        <div className="lg:col-span-2 space-y-2">
          <div className="rounded-2xl overflow-hidden shadow-md border border-slate-200" style={{ height: 520 }}>
            {loading ? (
              <div className="h-full flex items-center justify-center bg-slate-50">
                <div className="text-center">
                  <div className="text-5xl mb-3">🗺️</div>
                  <p className="text-slate-500 text-sm animate-pulse">Cargando mapa…</p>
                </div>
              </div>
            ) : error ? (
              <div className="h-full flex items-center justify-center bg-slate-50">
                <div className="text-center">
                  <p className="text-slate-600 text-sm font-semibold">{error}</p>
                  <Button variant="primary" size="sm" className="mt-4" onClick={loadData}>Reintentar</Button>
                </div>
              </div>
            ) : (
              <MapContainer center={[-33.4489, -70.6693]} zoom={11} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <MapClickCapture onMapClick={(lat, lng) => setClickPos({ lat, lng })} />

                {/* Report markers */}
                {reportes.map((r) => (
                  <Marker
                    key={r.id}
                    position={[r.ubicacion.latitud, r.ubicacion.longitud]}
                    icon={r.tipo === 'PERDIDO' ? LOST_ICON : FOUND_ICON}
                  >
                    <Popup>
                      <div style={{ minWidth: 200 }}>
                        {r.mascota?.fotografia && (
                          <img
                            src={r.mascota.fotografia}
                            alt={r.mascota.nombre}
                            style={{ width: '100%', height: 110, objectFit: 'cover', borderRadius: 8, marginBottom: 8 }}
                          />
                        )}
                        <span style={{
                          display: 'inline-block', padding: '2px 8px', borderRadius: 9999,
                          fontSize: 11, fontWeight: 600, color: 'white', marginBottom: 6,
                          background: r.tipo === 'PERDIDO' ? '#ef4444' : '#22c55e',
                        }}>
                          {r.tipo === 'PERDIDO' ? '😟 Perdida' : '😊 Encontrada'}
                        </span>
                        <p style={{ fontWeight: 700, fontSize: 13, margin: '0 0 2px' }}>{r.mascota?.nombre}</p>
                        <p style={{ fontSize: 11, color: '#64748b', margin: '0 0 2px' }}>
                          {r.mascota?.especie} · {r.mascota?.raza}
                        </p>
                        <p style={{ fontSize: 11, color: '#64748b', margin: '0 0 8px' }}>📍 {r.ubicacion?.ciudad}</p>
                        <button
                          onClick={() => navigate(`/reportes/${r.id}`)}
                          style={{
                            width: '100%', background: '#3b82f6', color: 'white', border: 'none',
                            borderRadius: 8, padding: '6px 0', fontSize: 11, fontWeight: 600, cursor: 'pointer',
                          }}
                        >
                          Ver detalles →
                        </button>
                      </div>
                    </Popup>
                  </Marker>
                ))}

                {/* Zone circles */}
                {zonas.map((z) =>
                  z.latitud && z.longitud ? (
                    <CircleMarker
                      key={z.id}
                      center={[z.latitud, z.longitud]}
                      radius={Math.min(20, 8 + z.cantidadReportesActivos)}
                      fillColor="#f59e0b"
                      color="white"
                      weight={2}
                      fillOpacity={0.3}
                    >
                      <Popup>
                        <div>
                          <p style={{ fontWeight: 700, fontSize: 13, margin: '0 0 2px' }}>{z.nombre}</p>
                          <p style={{ fontSize: 11, color: '#64748b' }}>{z.cantidadReportesActivos} incidencias</p>
                          <p style={{ fontSize: 11, color: '#64748b' }}>Radio: {z.radio} km</p>
                        </div>
                      </Popup>
                    </CircleMarker>
                  ) : null
                )}

                {/* Click popup */}
                {clickPos && (
                  <Popup
                    position={[clickPos.lat, clickPos.lng]}
                    eventHandlers={{ remove: () => setClickPos(null) }}
                  >
                    <div style={{ minWidth: 190 }}>
                      <p style={{ fontSize: 11, color: '#64748b', marginBottom: 8 }}>
                        📍 {clickPos.lat.toFixed(5)}, {clickPos.lng.toFixed(5)}
                      </p>
                      <button
                        onClick={() => navigate(`/reportes/crear?lat=${clickPos.lat}&lng=${clickPos.lng}`)}
                        style={{
                          width: '100%', background: '#f59e0b', color: 'white', border: 'none',
                          borderRadius: 8, padding: '7px 0', fontSize: 12, fontWeight: 700, cursor: 'pointer',
                        }}
                      >
                        + Crear reporte aquí
                      </button>
                    </div>
                  </Popup>
                )}
              </MapContainer>
            )}
          </div>

          {/* Legend */}
          <div className="flex items-center gap-4 flex-wrap text-xs text-slate-500 px-1">
            <div className="flex items-center gap-1.5">
              <div className="w-3 h-3 rounded-full bg-red-500" /> Perdida
            </div>
            <div className="flex items-center gap-1.5">
              <div className="w-3 h-3 rounded-full bg-emerald-500" /> Encontrada
            </div>
            <div className="flex items-center gap-1.5">
              <div className="w-4 h-4 rounded-full bg-amber-400 opacity-50" /> Zona de incidencia
            </div>
            <span className="text-slate-400">· Clic en el mapa para crear un reporte</span>
          </div>
        </div>

        {/* Sidebar */}
        <div className="space-y-4 overflow-y-auto max-h-[540px] pr-0.5">
          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <Card key={i} className="animate-pulse">
                  <div className="h-20 bg-slate-100 rounded-xl" />
                </Card>
              ))}
            </div>
          ) : (
            <>
              {/* Summary */}
              <Card>
                <div className="flex items-center gap-2 mb-3">
                  <div className="w-7 h-7 bg-primary-50 rounded-lg flex items-center justify-center">
                    <FiMapPin size={14} className="text-primary-500" />
                  </div>
                  <h3 className="font-bold text-slate-800 text-sm">Reportes activos</h3>
                </div>
                {reportes.length === 0 ? (
                  <p className="text-xs text-slate-400 text-center py-4">
                    No hay reportes activos aún.<br />
                    <button
                      onClick={() => navigate('/reportes/crear')}
                      className="text-primary-500 font-semibold hover:underline mt-1"
                    >
                      + Crear el primero
                    </button>
                  </p>
                ) : (
                  <div className="grid grid-cols-2 gap-2">
                    <div className="bg-red-50 rounded-xl p-2.5 text-center">
                      <p className="text-xl font-bold text-red-600">{lost.length}</p>
                      <p className="text-xs text-red-500 font-medium">Perdidas</p>
                    </div>
                    <div className="bg-emerald-50 rounded-xl p-2.5 text-center">
                      <p className="text-xl font-bold text-emerald-600">{found.length}</p>
                      <p className="text-xs text-emerald-500 font-medium">Encontradas</p>
                    </div>
                  </div>
                )}
              </Card>

              {/* Zones */}
              {zonas.length > 0 && (
                <Card>
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-7 h-7 bg-amber-50 rounded-lg flex items-center justify-center">
                      <FiAlertTriangle size={14} className="text-amber-500" />
                    </div>
                    <h3 className="font-bold text-slate-800 text-sm">Zonas de Incidencia</h3>
                  </div>
                  <div className="space-y-2">
                    {zonas.map((z) => (
                      <div key={z.id} className="p-2.5 bg-slate-50 rounded-xl">
                        <p className="font-semibold text-slate-800 text-xs mb-1">{z.nombre}</p>
                        <p className="text-xs text-slate-500">
                          📏 Radio: {z.radio} km · ⚠️ {z.cantidadReportesActivos} incidencias
                        </p>
                      </div>
                    ))}
                  </div>
                </Card>
              )}

              {/* Instructions */}
              <Card className="bg-primary-50 border-primary-100">
                <p className="text-xs font-semibold text-primary-700 mb-2">¿Cómo usar el mapa?</p>
                <ul className="text-xs text-primary-600 space-y-1.5">
                  <li>🖱️ Haz clic en cualquier punto del mapa</li>
                  <li>📌 Selecciona Crear reporte aqui</li>
                  <li>🐾 Las coordenadas se llenan automáticamente</li>
                  <li>🔴 Marcadores rojos = mascotas perdidas</li>
                  <li>🟢 Marcadores verdes = mascotas encontradas</li>
                </ul>
              </Card>
            </>
          )}
        </div>
      </div>
    </div>
  )
}

export default GeolocalizacionPage
