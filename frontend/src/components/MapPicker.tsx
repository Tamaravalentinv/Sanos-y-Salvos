import React, { Component } from 'react'
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

delete (L.Icon.Default.prototype as any)._getIconUrl
L.Icon.Default.mergeOptions({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})

interface Props {
  lat?: number
  lng?: number
  height?: number
  onLocationSelect: (lat: number, lng: number, address: string, city: string) => void
}

interface ErrorBoundaryState { hasError: boolean }

class MapErrorBoundary extends Component<
  { children: React.ReactNode; height: number },
  ErrorBoundaryState
> {
  state: ErrorBoundaryState = { hasError: false }
  static getDerivedStateFromError() { return { hasError: true } }
  render() {
    if (this.state.hasError) {
      return (
        <div
          className="rounded-xl border border-slate-200 bg-slate-50 flex flex-col items-center justify-center gap-3 text-slate-400"
          style={{ height: this.props.height }}
        >
          <span className="text-4xl">🗺️</span>
          <p className="text-sm font-medium">No se pudo cargar el mapa</p>
          <p className="text-xs">Escribe la ciudad manualmente</p>
        </div>
      )
    }
    return this.props.children
  }
}

const ClickHandler: React.FC<{ onSelect: Props['onLocationSelect'] }> = ({ onSelect }) => {
  useMapEvents({
    async click(e) {
      const { lat, lng } = e.latlng
      try {
        const res = await fetch(
          `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`,
          { headers: { 'Accept-Language': 'es' } }
        )
        const data = await res.json()
        const addr = data.address || {}
        const city = addr.city || addr.town || addr.village || addr.county || ''
        const road = [addr.road, addr.house_number].filter(Boolean).join(' ')
        onSelect(lat, lng, road, city)
      } catch {
        onSelect(lat, lng, '', '')
      }
    },
  })
  return null
}

const MapInner: React.FC<Props> = ({ lat, lng, height = 300, onLocationSelect }) => {
  const hasPos = typeof lat === 'number' && typeof lng === 'number' && !isNaN(lat) && !isNaN(lng) && (lat !== 0 || lng !== 0)
  const center: [number, number] = hasPos ? [lat!, lng!] : [-33.4489, -70.6693]
  const zoom = hasPos ? 15 : 12

  return (
    <div
      className="rounded-xl overflow-hidden border border-slate-200 shadow-sm"
      style={{ height }}
    >
      {/* key changes when we go from no-position → has-position, forcing re-center */}
      <MapContainer
        key={hasPos ? `map-${lat!.toFixed(4)}-${lng!.toFixed(4)}` : 'map-init'}
        center={center}
        zoom={zoom}
        style={{ height: '100%', width: '100%' }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <ClickHandler onSelect={onLocationSelect} />
        {hasPos && <Marker position={[lat!, lng!]} />}
      </MapContainer>
    </div>
  )
}

const MapPicker: React.FC<Props> = (props) => (
  <MapErrorBoundary height={props.height ?? 300}>
    <MapInner {...props} />
  </MapErrorBoundary>
)

export default MapPicker
