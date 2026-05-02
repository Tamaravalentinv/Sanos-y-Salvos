export const formatDate = (dateString: string): string => {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

export const formatDateTime = (dateString: string): string => {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

export const formatDistanceToNow = (dateString: string): string => {
  const date = new Date(dateString)
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffMins = Math.floor(diffMs / 60000)
  const diffHours = Math.floor(diffMs / 3600000)
  const diffDays = Math.floor(diffMs / 86400000)

  if (diffMins < 1) return 'Hace poco'
  if (diffMins < 60) return `Hace ${diffMins}m`
  if (diffHours < 24) return `Hace ${diffHours}h`
  if (diffDays < 7) return `Hace ${diffDays}d`
  
  return formatDate(dateString)
}

export const getInitials = (nombre: string, apellido: string): string => {
  return `${nombre.charAt(0)}${apellido.charAt(0)}`.toUpperCase()
}

export const calculateScoreColor = (score: number): string => {
  if (score >= 0.8) return 'text-green-600 bg-green-50'
  if (score >= 0.6) return 'text-blue-600 bg-blue-50'
  if (score >= 0.4) return 'text-yellow-600 bg-yellow-50'
  return 'text-red-600 bg-red-50'
}

export const calculateIntensityColor = (
  intensidad: 'BAJA' | 'MEDIA' | 'ALTA'
): string => {
  switch (intensidad) {
    case 'ALTA':
      return 'bg-red-500'
    case 'MEDIA':
      return 'bg-yellow-500'
    case 'BAJA':
      return 'bg-green-500'
  }
}

export const getStatusBadgeClass = (estado: string): string => {
  switch (estado) {
    case 'ACTIVO':
      return 'bg-blue-100 text-blue-800'
    case 'RESUELTO':
      return 'bg-green-100 text-green-800'
    case 'CANCELADO':
      return 'bg-gray-100 text-gray-800'
    case 'CONFIRMADA':
      return 'bg-green-100 text-green-800'
    case 'RECHAZADA':
      return 'bg-red-100 text-red-800'
    case 'SUGERENCIA':
      return 'bg-yellow-100 text-yellow-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}

export const distanceBetweenCoordinates = (
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
): number => {
  const R = 6371 // Earth's radius in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180
  const dLon = ((lon2 - lon1) * Math.PI) / 180
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2)
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return R * c
}
