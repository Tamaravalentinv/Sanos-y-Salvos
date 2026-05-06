import React, { useEffect, useState } from 'react'
import { FiTrash2, FiCheckSquare } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { useNotificacionStore } from '@/context/notificacionStore'
import { formatDistanceToNow } from '@/utils/helpers'
import toast from 'react-hot-toast'

const TIPO_ICON: Record<string, string> = {
  COINCIDENCIA: '🎯',
  RESOLUCION:   '✅',
  ALERTA:       '⚠️',
  INFORMACION:  'ℹ️',
}

const NotificacionesPage: React.FC = () => {
  const { notificaciones, loadNotificaciones, marcarTodasComoLeidas, deleteNotificacion, clearAll } = useNotificacionStore()
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState<'todas' | 'no_leidas'>('todas')

  useEffect(() => {
    loadNotificaciones().then(() => setLoading(false))
  }, [])

  const filtered = notificaciones.filter(n => filter === 'no_leidas' ? n.estado === 'NO_LEIDA' : true)
  const unreadCount = notificaciones.filter(n => n.estado === 'NO_LEIDA').length

  const handleMarkAllAsRead = async () => {
    await marcarTodasComoLeidas()
    toast.success('Todas las notificaciones marcadas como leídas')
  }

  const handleClearAll = async () => {
    if (window.confirm('¿Eliminar todas las notificaciones?')) {
      await clearAll()
      toast.success('Notificaciones eliminadas')
    }
  }

  return (
    <div className="space-y-5 fade-in max-w-3xl">
      {/* Header */}
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Notificaciones</h1>
          <p className="text-slate-500 text-sm mt-0.5">
            {unreadCount > 0 ? `${unreadCount} sin leer` : 'Todo al día'}
          </p>
        </div>
        {notificaciones.length > 0 && (
          <div className="flex gap-2">
            <Button variant="secondary" size="sm" onClick={handleMarkAllAsRead}>
              <FiCheckSquare size={14} /> Marcar leídas
            </Button>
            <Button variant="danger" size="sm" onClick={handleClearAll}>
              <FiTrash2 size={14} /> Limpiar todo
            </Button>
          </div>
        )}
      </div>

      {/* Filter tabs */}
      <div className="flex gap-1 bg-slate-100 p-1 rounded-xl w-fit">
        {(['todas', 'no_leidas'] as const).map(tab => (
          <button
            key={tab}
            onClick={() => setFilter(tab)}
            className={`px-4 py-1.5 rounded-lg text-sm font-semibold transition-all ${
              filter === tab
                ? 'bg-white text-primary-700 shadow-sm'
                : 'text-slate-500 hover:text-slate-700'
            }`}
          >
            {tab === 'todas' ? 'Todas' : 'No leídas'}
            {tab === 'no_leidas' && unreadCount > 0 && (
              <span className="ml-1.5 bg-primary-500 text-white text-xs px-1.5 py-0.5 rounded-full">
                {unreadCount}
              </span>
            )}
          </button>
        ))}
      </div>

      {/* List */}
      <div className="space-y-2">
        {loading ? (
          <div className="space-y-2">
            {[1, 2, 3, 4].map(i => (
              <Card key={i} className="animate-pulse">
                <div className="flex gap-4">
                  <div className="w-10 h-10 bg-slate-100 rounded-xl flex-shrink-0" />
                  <div className="flex-1 space-y-2">
                    <div className="h-4 bg-slate-100 rounded w-1/2" />
                    <div className="h-3 bg-slate-100 rounded w-3/4" />
                  </div>
                </div>
              </Card>
            ))}
          </div>
        ) : filtered.length === 0 ? (
          <Card className="text-center py-20">
            <div className="text-6xl mb-4">📭</div>
            <p className="text-slate-600 font-semibold text-lg">
              {filter === 'no_leidas' ? 'Sin notificaciones no leídas' : 'Bandeja vacía'}
            </p>
            <p className="text-slate-400 text-sm mt-1">Te notificaremos cuando haya novedades</p>
          </Card>
        ) : (
          filtered.map(n => (
            <Card
              key={n.id}
              className={`transition-all ${n.estado === 'NO_LEIDA' ? 'border-l-4 border-l-primary-400 bg-primary-50/30' : ''}`}
            >
              <div className="flex items-start gap-4">
                <div className={`w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 text-xl
                  ${n.tipo === 'COINCIDENCIA' ? 'bg-accent-50' :
                    n.tipo === 'RESOLUCION'   ? 'bg-emerald-50' :
                    n.tipo === 'ALERTA'       ? 'bg-red-50' : 'bg-primary-50'}`}>
                  {TIPO_ICON[n.tipo] ?? 'ℹ️'}
                </div>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 flex-wrap mb-0.5">
                    <p className="font-semibold text-slate-800 text-sm">{n.titulo}</p>
                    <Badge status={n.tipo}>{n.tipo}</Badge>
                    {n.estado === 'NO_LEIDA' && (
                      <span className="w-2 h-2 bg-primary-500 rounded-full" />
                    )}
                  </div>
                  <p className="text-sm text-slate-600 mb-1">{n.mensaje}</p>
                  <p className="text-xs text-slate-400">{formatDistanceToNow(n.fechaCreacion)}</p>
                </div>

                <button
                  onClick={() => deleteNotificacion(n.id)}
                  className="p-2 hover:bg-slate-100 rounded-lg transition flex-shrink-0 text-slate-400 hover:text-red-500"
                  title="Eliminar"
                >
                  <FiTrash2 size={16} />
                </button>
              </div>
            </Card>
          ))
        )}
      </div>
    </div>
  )
}

export default NotificacionesPage
