import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { FiArrowLeft, FiMapPin, FiCalendar, FiTag, FiCheckCircle, FiMessageSquare, FiSend } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { reporteService } from '@/services/reporte.service'
import { useAuthStore } from '@/context/authStore'
import { useNotificacionStore } from '@/context/notificacionStore'
import { useMensajeStore } from '@/context/mensajeStore'
import { Reporte, Notificacion } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const ReporteDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const { notificaciones, addNotificacion } = useNotificacionStore()
  const { enviarMensaje } = useMensajeStore()
  const [reporte, setReporte] = useState<Reporte | null>(null)
  const [loading, setLoading] = useState(true)
  const [resolving, setResolving] = useState(false)
  const [showContact, setShowContact] = useState(false)
  const [contactMsg, setContactMsg] = useState('')
  const [sendingMsg, setSendingMsg] = useState(false)

  useEffect(() => {
    if (!id) return
    loadReporte(id)
  }, [id])

  const loadReporte = async (reporteId: string) => {
    setLoading(true)
    try {
      const data = await reporteService.getReporteById(reporteId)
      setReporte(data)
    } catch {
      toast.error('No se pudo cargar el reporte')
      navigate('/reportes')
    } finally {
      setLoading(false)
    }
  }

  const handleResolver = async () => {
    if (!reporte || !id) return
    if (!window.confirm('¿Marcar este reporte como resuelto? Esto indica que la mascota fue encontrada o devuelta.')) return
    setResolving(true)
    try {
      await reporteService.resolverReporte(id)
      setReporte(prev => prev ? { ...prev, estado: 'RESUELTO' } : prev)

      // Notificación local de resolución
      const notif: Notificacion = {
        id: `resolucion_${Date.now()}`,
        titulo: '✅ Reporte resuelto',
        mensaje: `El reporte de "${reporte.mascota?.nombre ?? 'tu mascota'}" ha sido marcado como resuelto. ¡Nos alegra que haya terminado bien!`,
        tipo: 'RESOLUCION',
        estado: 'NO_LEIDA',
        usuarioId: user?.id ?? '',
        relatedReporteId: id,
        fechaCreacion: new Date().toISOString(),
        canalEnvio: 'INTERNO',
      }
      addNotificacion(notif)
      toast.success('¡Reporte resuelto! Nos alegra que todo haya terminado bien 🐾')
    } catch {
      toast.error('No se pudo actualizar el estado del reporte')
    } finally {
      setResolving(false)
    }
  }

  const handleContactar = async () => {
    if (!reporte || !id || !user || !contactMsg.trim()) return
    setSendingMsg(true)
    enviarMensaje({
      fromUserId: user.id,
      fromUserName: `${user.nombre} ${user.apellido}`,
      toUserId: reporte.usuarioId,
      toUserName: 'Dueño del reporte',
      reporteId: id,
      reporteTitulo: reporte.titulo,
      contenido: contactMsg.trim(),
    })
    setContactMsg('')
    setShowContact(false)
    toast.success('Mensaje enviado. El dueño lo verá en su bandeja de mensajes.')
    setSendingMsg(false)
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-500 mx-auto mb-3" />
          <p className="text-slate-500 text-sm">Cargando reporte…</p>
        </div>
      </div>
    )
  }

  if (!reporte) return null

  const especieEmoji = reporte.mascota?.especie === 'PERRO' ? '🐕' : reporte.mascota?.especie === 'GATO' ? '🐈' : '🐾'
  const esOwner = user?.id === reporte.usuarioId
  const matchNotifs = notificaciones.filter(n => n.tipo === 'COINCIDENCIA' && n.relatedReporteId !== id)

  return (
    <div className="space-y-5 max-w-3xl mx-auto fade-in">
      {/* Header */}
      <div className="flex items-center gap-3">
        <Button variant="ghost" size="sm" onClick={() => navigate(-1)}>
          <FiArrowLeft size={16} /> Volver
        </Button>
        <div>
          <h1 className="text-xl font-bold text-slate-900">Detalle del Reporte</h1>
          <p className="text-slate-500 text-sm">{reporte.titulo}</p>
        </div>
      </div>

      {/* Main card */}
      <Card padding="none" className="overflow-hidden">
        {/* Photo or gradient */}
        {reporte.mascota?.fotografia ? (
          <img src={reporte.mascota.fotografia} alt={reporte.mascota.nombre}
            className="w-full h-72 object-cover" />
        ) : (
          <div className="w-full h-48 bg-gradient-to-br from-primary-50 to-primary-100 flex items-center justify-center">
            <span className="text-8xl opacity-40">{especieEmoji}</span>
          </div>
        )}

        <div className="p-6 space-y-5">
          {/* Title + badges */}
          <div className="flex items-start justify-between gap-4 flex-wrap">
            <div>
              <h2 className="text-2xl font-bold text-slate-900">{reporte.mascota?.nombre ?? 'Sin nombre'}</h2>
              <p className="text-slate-500 text-sm mt-0.5">{reporte.mascota?.especie} · {reporte.mascota?.raza}</p>
            </div>
            <div className="flex gap-2">
              <Badge status={reporte.tipo}>{reporte.tipo}</Badge>
              <Badge status={reporte.estado}>{reporte.estado}</Badge>
            </div>
          </div>

          {/* Detail grid */}
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            {[
              { label: 'Color', value: reporte.mascota?.color || '—' },
              { label: 'Tamaño', value: reporte.mascota?.tamaño || '—' },
              { label: 'Edad', value: reporte.mascota?.edad ? `${reporte.mascota.edad} años` : '—' },
            ].map(item => (
              <div key={item.label} className="bg-slate-50 rounded-xl px-4 py-3">
                <p className="text-xs text-slate-400 font-semibold uppercase tracking-wide mb-0.5">{item.label}</p>
                <p className="font-semibold text-slate-800 text-sm">{item.value}</p>
              </div>
            ))}
          </div>

          {/* Location & date */}
          <div className="flex flex-wrap gap-4 text-sm text-slate-600">
            <div className="flex items-center gap-1.5">
              <FiMapPin size={15} className="text-primary-500" />
              <span>{reporte.ubicacion?.ciudad ?? '—'}{reporte.ubicacion?.direccion ? ` — ${reporte.ubicacion.direccion}` : ''}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <FiCalendar size={15} className="text-primary-500" />
              <span>{formatDate(reporte.fechaReporte)}</span>
            </div>
          </div>

          {/* Description */}
          {reporte.descripcion && (
            <div>
              <p className="text-xs font-semibold text-slate-400 uppercase tracking-wide mb-1.5">Descripción</p>
              <p className="text-slate-700 text-sm leading-relaxed">{reporte.descripcion}</p>
            </div>
          )}

          {/* Special marks */}
          {reporte.mascota?.señas_particulares && (
            <div className="bg-amber-50 border border-amber-100 rounded-xl p-4">
              <div className="flex items-center gap-2 mb-1">
                <FiTag size={14} className="text-amber-600" />
                <p className="text-xs font-semibold text-amber-700 uppercase tracking-wide">Señas Particulares</p>
              </div>
              <p className="text-sm text-amber-900">{reporte.mascota.señas_particulares}</p>
            </div>
          )}

          {/* Resolver button — visible solo al dueño cuando el reporte está activo */}
          {esOwner && reporte.estado === 'ACTIVO' && (
            <div className="border-t border-slate-100 pt-4">
              <p className="text-xs text-slate-400 mb-3">¿Ya se encontró tu mascota? Marca el reporte como resuelto para notificar a la comunidad.</p>
              <Button
                variant="accent"
                className="w-full"
                onClick={handleResolver}
                loading={resolving}
              >
                <FiCheckCircle size={16} /> Marcar como Resuelto — ¡La encontré!
              </Button>
            </div>
          )}

          {/* Contactar al dueño — visible para no-dueños en reportes activos */}
          {!esOwner && reporte.estado === 'ACTIVO' && (
            <div className="border-t border-slate-100 pt-4 space-y-3">
              {!showContact ? (
                <Button
                  variant="secondary"
                  className="w-full"
                  onClick={() => setShowContact(true)}
                >
                  <FiMessageSquare size={16} /> Contactar al dueño
                </Button>
              ) : (
                <div className="space-y-3">
                  <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
                    Enviar mensaje al dueño del reporte
                  </p>
                  <textarea
                    value={contactMsg}
                    onChange={e => setContactMsg(e.target.value)}
                    placeholder={`Hola, creo que encontré a ${reporte.mascota?.nombre ?? 'tu mascota'}. Me gustaría coordinarnos…`}
                    rows={4}
                    className="w-full border border-slate-200 rounded-xl px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-300 resize-none"
                  />
                  <div className="flex gap-2">
                    <Button variant="ghost" size="sm" onClick={() => { setShowContact(false); setContactMsg('') }}>
                      Cancelar
                    </Button>
                    <Button
                      variant="primary"
                      size="sm"
                      onClick={handleContactar}
                      loading={sendingMsg}
                      disabled={!contactMsg.trim()}
                    >
                      <FiSend size={14} /> Enviar mensaje
                    </Button>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Resuelto banner */}
          {reporte.estado === 'RESUELTO' && (
            <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-4 flex gap-3 items-center">
              <span className="text-2xl">🎉</span>
              <div>
                <p className="font-bold text-emerald-800 text-sm">¡Caso resuelto!</p>
                <p className="text-xs text-emerald-700 mt-0.5">Esta mascota ya fue encontrada o devuelta a su dueño.</p>
              </div>
            </div>
          )}
        </div>
      </Card>

      {/* Coincidencias relacionadas */}
      {matchNotifs.length > 0 && (
        <Card>
          <h3 className="font-bold text-slate-800 text-sm mb-3">🎯 Posibles coincidencias detectadas</h3>
          <div className="space-y-2">
            {matchNotifs.slice(0, 3).map(n => (
              <div
                key={n.id}
                className="bg-accent-50 border border-accent-100 rounded-xl p-3 cursor-pointer hover:bg-accent-100 transition"
                onClick={() => n.relatedReporteId && navigate(`/reportes/${n.relatedReporteId}`)}
              >
                <p className="text-sm font-semibold text-accent-800">{n.titulo}</p>
                <p className="text-xs text-accent-700 mt-0.5">{n.mensaje}</p>
                {n.relatedReporteId && (
                  <p className="text-xs text-accent-500 mt-1 font-medium">Ver reporte coincidente →</p>
                )}
              </div>
            ))}
          </div>
        </Card>
      )}
    </div>
  )
}

export default ReporteDetailPage
