import React, { useEffect, useState } from 'react'
import { FiTrash2, FiCheckSquare } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { notificacionService } from '@/services/notificacion.service'
import { useNotificacionStore } from '@/context/notificacionStore'
import { Notificacion } from '@/types'
import { formatDistanceToNow } from '@/utils/helpers'
import toast from 'react-hot-toast'

const NotificacionesPage: React.FC = () => {
  const {
    notificaciones,
    loadNotificaciones,
    marcarTodasComoLeidas,
    deleteNotificacion,
    clearAll,
  } = useNotificacionStore()
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState<'todas' | 'no_leidas'>('todas')

  useEffect(() => {
    loadNotificaciones().then(() => setLoading(false))
  }, [])

  const filteredNotificaciones = notificaciones.filter((n) => {
    if (filter === 'no_leidas') return n.estado === 'NO_LEIDA'
    return true
  })

  const handleMarkAllAsRead = async () => {
    await marcarTodasComoLeidas()
    toast.success('Todas las notificaciones marcadas como leídas')
  }

  const handleClearAll = async () => {
    if (window.confirm('¿Estás seguro de que deseas eliminar todas las notificaciones?')) {
      await clearAll()
      toast.success('Todas las notificaciones eliminadas')
    }
  }

  const getNotificacionIcon = (tipo: string): string => {
    switch (tipo) {
      case 'COINCIDENCIA':
        return '🎯'
      case 'RESOLUCION':
        return '✅'
      case 'ALERTA':
        return '⚠️'
      case 'INFORMACION':
      default:
        return 'ℹ️'
    }
  }

  const getNotificacionTypeColor = (tipo: string): string => {
    switch (tipo) {
      case 'COINCIDENCIA':
        return 'bg-purple-100 text-purple-800'
      case 'RESOLUCION':
        return 'bg-green-100 text-green-800'
      case 'ALERTA':
        return 'bg-red-100 text-red-800'
      case 'INFORMACION':
      default:
        return 'bg-blue-100 text-blue-800'
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Notificaciones</h1>
          <p className="text-gray-600 mt-1">
            Mantente actualizado sobre tus reportes y coincidencias
          </p>
        </div>
        {notificaciones.length > 0 && (
          <div className="flex gap-2">
            <Button
              variant="secondary"
              size="sm"
              onClick={handleMarkAllAsRead}
            >
              <FiCheckSquare size={18} />
              Marcar todo como leído
            </Button>
            <Button
              variant="danger"
              size="sm"
              onClick={handleClearAll}
            >
              <FiTrash2 size={18} />
              Limpiar
            </Button>
          </div>
        )}
      </div>

      {/* Filters */}
      <Card>
        <div className="flex gap-2">
          <button
            onClick={() => setFilter('todas')}
            className={`px-4 py-2 rounded-lg font-semibold transition ${
              filter === 'todas'
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 text-gray-900 hover:bg-gray-200'
            }`}
          >
            Todas
          </button>
          <button
            onClick={() => setFilter('no_leidas')}
            className={`px-4 py-2 rounded-lg font-semibold transition ${
              filter === 'no_leidas'
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 text-gray-900 hover:bg-gray-200'
            }`}
          >
            No Leídas
          </button>
        </div>
      </Card>

      {/* Notifications List */}
      <div className="space-y-3">
        {loading ? (
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto mb-4"></div>
              <p className="text-gray-600">Cargando notificaciones...</p>
            </div>
          </div>
        ) : filteredNotificaciones.length > 0 ? (
          filteredNotificaciones.map((notificacion) => (
            <Card
              key={notificacion.id}
              className={notificacion.estado === 'NO_LEIDA' ? 'bg-gray-50 border-2' : ''}
            >
              <div className="flex items-start justify-between gap-4">
                <div className="flex items-start gap-4 flex-1">
                  {/* Icon */}
                  <span className="text-3xl flex-shrink-0">
                    {getNotificacionIcon(notificacion.tipo)}
                  </span>

                  {/* Content */}
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <h3 className="font-semibold text-gray-900">
                        {notificacion.titulo}
                      </h3>
                      <Badge status={notificacion.tipo}>
                        {notificacion.tipo}
                      </Badge>
                      {notificacion.estado === 'NO_LEIDA' && (
                        <span className="w-2 h-2 bg-primary-600 rounded-full"></span>
                      )}
                    </div>
                    <p className="text-gray-700 mb-2">{notificacion.mensaje}</p>
                    <p className="text-xs text-gray-500">
                      {formatDistanceToNow(notificacion.fechaCreacion)}
                    </p>
                  </div>
                </div>

                {/* Action */}
                <button
                  onClick={() => deleteNotificacion(notificacion.id)}
                  className="p-2 hover:bg-gray-100 rounded-lg transition flex-shrink-0"
                  title="Eliminar"
                >
                  <FiTrash2 size={18} className="text-gray-400" />
                </button>
              </div>
            </Card>
          ))
        ) : (
          <Card>
            <div className="text-center py-12">
              <p className="text-4xl mb-4">📭</p>
              <p className="text-gray-500 text-lg">
                {filter === 'no_leidas'
                  ? 'No hay notificaciones no leídas'
                  : 'No hay notificaciones'}
              </p>
            </div>
          </Card>
        )}
      </div>
    </div>
  )
}

export default NotificacionesPage
