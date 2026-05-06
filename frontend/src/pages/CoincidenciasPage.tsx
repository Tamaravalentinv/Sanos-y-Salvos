import React, { useEffect, useState } from 'react'
import { FiCheckCircle, FiXCircle, FiGitMerge, FiTrendingUp, FiTarget } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { coincidenciaService } from '@/services/coincidencia.service'
import { Coincidencia } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const ScoreRing = ({ score }: { score: number }) => {
  const pct = Math.round(score * 100)
  const color = pct >= 80 ? 'text-emerald-600' : pct >= 60 ? 'text-amber-500' : 'text-red-500'
  return (
    <div className="flex flex-col items-center gap-1">
      <div className={`text-3xl font-bold ${color}`}>{pct}%</div>
      <div className="text-xs text-slate-500 font-medium">similitud</div>
    </div>
  )
}

const CoincidenciasPage: React.FC = () => {
  const [coincidencias, setCoincidenias] = useState<Coincidencia[]>([])
  const [stats, setStats] = useState<any>(null)
  const [loading, setLoading] = useState(true)
  const [actionLoading, setActionLoading] = useState<string | null>(null)

  useEffect(() => { loadData() }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [coincsData, statsData] = await Promise.all([
        coincidenciaService.getCoincidendasRecientes(50),
        coincidenciaService.getEstadisticas(),
      ])
      setCoincidenias(coincsData)
      setStats(statsData)
    } catch {
      toast.error('Error al cargar coincidencias')
    } finally {
      setLoading(false)
    }
  }

  const handleConfirm = async (coincidenciaId: string) => {
    setActionLoading(coincidenciaId)
    try {
      const updated = await coincidenciaService.confirmarCoincidencia(coincidenciaId)
      setCoincidenias(prev => prev.map(c => c.id === coincidenciaId ? updated : c))
      toast.success('¡Coincidencia confirmada!')
    } catch {
      toast.error('Error al confirmar coincidencia')
    } finally {
      setActionLoading(null)
    }
  }

  const handleReject = async (coincidenciaId: string) => {
    setActionLoading(coincidenciaId)
    try {
      await coincidenciaService.rechazarCoincidencia(coincidenciaId)
      setCoincidenias(prev => prev.filter(c => c.id !== coincidenciaId))
      toast.success('Coincidencia rechazada')
    } catch {
      toast.error('Error al rechazar coincidencia')
    } finally {
      setActionLoading(null)
    }
  }

  return (
    <div className="space-y-6 fade-in">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Coincidencias</h1>
        <p className="text-slate-500 text-sm mt-0.5">Motor de matching inteligente para reunir mascotas con sus familias</p>
      </div>

      {/* Stats */}
      {stats && (
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <Card className="relative overflow-hidden">
            <div className="absolute -top-4 -right-4 w-20 h-20 bg-primary-500 rounded-full opacity-10" />
            <div className="flex items-start justify-between">
              <div>
                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Total Coincidencias</p>
                <p className="text-3xl font-bold text-slate-900 mt-1">{stats.totalCoincidencias}</p>
              </div>
              <div className="w-10 h-10 bg-primary-50 rounded-xl flex items-center justify-center">
                <FiGitMerge size={20} className="text-primary-600" />
              </div>
            </div>
          </Card>

          <Card className="relative overflow-hidden">
            <div className="absolute -top-4 -right-4 w-20 h-20 bg-emerald-500 rounded-full opacity-10" />
            <div className="flex items-start justify-between">
              <div>
                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Confirmadas</p>
                <p className="text-3xl font-bold text-slate-900 mt-1">{stats.coincidenciasConfirmadas}</p>
              </div>
              <div className="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
                <FiCheckCircle size={20} className="text-emerald-600" />
              </div>
            </div>
          </Card>

          <Card className="relative overflow-hidden">
            <div className="absolute -top-4 -right-4 w-20 h-20 bg-accent-500 rounded-full opacity-10" />
            <div className="flex items-start justify-between">
              <div>
                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Tasa de Éxito</p>
                <p className="text-3xl font-bold text-slate-900 mt-1">{Math.round(stats.tasaExito * 100)}%</p>
              </div>
              <div className="w-10 h-10 bg-accent-50 rounded-xl flex items-center justify-center">
                <FiTrendingUp size={20} className="text-accent-600" />
              </div>
            </div>
          </Card>
        </div>
      )}

      {/* List */}
      <div className="space-y-4">
        {loading ? (
          <div className="space-y-4">
            {[1, 2, 3].map(i => (
              <Card key={i} className="animate-pulse">
                <div className="h-32 bg-slate-100 rounded-xl" />
              </Card>
            ))}
          </div>
        ) : coincidencias.length === 0 ? (
          <Card className="text-center py-20">
            <div className="text-6xl mb-4">🔗</div>
            <p className="text-slate-600 font-semibold text-lg">Sin coincidencias pendientes</p>
            <p className="text-slate-400 text-sm mt-1">El motor revisará nuevos reportes automáticamente</p>
          </Card>
        ) : (
          coincidencias.map(coincidencia => {
            const isPending = coincidencia.estado === 'SUGERENCIA'
            return (
              <Card key={coincidencia.id} className={`border-2 ${isPending ? 'border-accent-200' : 'border-slate-100'}`}>
                <div className="space-y-4">
                  {/* Card header */}
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-accent-50 rounded-xl flex items-center justify-center">
                        <FiTarget size={20} className="text-accent-600" />
                      </div>
                      <div>
                        <h3 className="font-bold text-slate-800">¡Posible coincidencia encontrada!</h3>
                        <p className="text-xs text-slate-500">{formatDate(coincidencia.fechaDeteccion)}</p>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Badge status={coincidencia.estado}>{coincidencia.estado}</Badge>
                      <ScoreRing score={coincidencia.scoreMatching} />
                    </div>
                  </div>

                  {/* Reports comparison */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {[coincidencia.reportePerdido, coincidencia.reporteEncontrado].map((reporte, idx) => (
                      <div key={idx} className={`p-4 rounded-xl border ${idx === 0 ? 'bg-red-50 border-red-100' : 'bg-emerald-50 border-emerald-100'}`}>
                        <p className={`text-xs font-semibold mb-2 ${idx === 0 ? 'text-red-600' : 'text-emerald-600'}`}>
                          {reporte.tipo === 'PERDIDO' ? '🔴 Mascota Perdida' : '🟢 Mascota Encontrada'}
                        </p>
                        <p className="font-bold text-slate-900">{reporte.mascota.nombre}</p>
                        <p className="text-sm text-slate-600">{reporte.mascota.raza} · {reporte.mascota.color}</p>
                        <p className="text-xs text-slate-500 mt-1">📍 {reporte.ubicacion.ciudad}</p>
                      </div>
                    ))}
                  </div>

                  {/* Matching factors */}
                  {coincidencia.factoresCoincidencia.length > 0 && (
                    <div>
                      <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-2">Factores detectados</p>
                      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                        {coincidencia.factoresCoincidencia.map((factor, idx) => (
                          <div key={idx} className="flex items-center justify-between bg-slate-50 rounded-lg px-3 py-2">
                            <span className="text-xs text-slate-700">{factor.factor}: {factor.detalle}</span>
                            <span className="text-xs font-bold text-primary-600 ml-2">{Math.round(factor.score * 100)}%</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* Actions */}
                  {isPending && (
                    <div className="flex gap-2 pt-2 border-t border-slate-100">
                      <Button
                        variant="primary"
                        size="sm"
                        onClick={() => handleConfirm(coincidencia.id)}
                        loading={actionLoading === coincidencia.id}
                        className="flex-1"
                      >
                        <FiCheckCircle size={16} /> Confirmar
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleReject(coincidencia.id)}
                        loading={actionLoading === coincidencia.id}
                        className="flex-1"
                      >
                        <FiXCircle size={16} /> Rechazar
                      </Button>
                    </div>
                  )}
                </div>
              </Card>
            )
          })
        )}
      </div>
    </div>
  )
}

export default CoincidenciasPage
