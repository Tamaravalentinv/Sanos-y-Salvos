import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { FiArrowLeft, FiMapPin, FiCalendar, FiTag } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { reporteService } from '@/services/reporte.service'
import { Reporte } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const ReporteDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [reporte, setReporte] = useState<Reporte | null>(null)
  const [loading, setLoading] = useState(true)

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
        </div>
      </Card>
    </div>
  )
}

export default ReporteDetailPage
