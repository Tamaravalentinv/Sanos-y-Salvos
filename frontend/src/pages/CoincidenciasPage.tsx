import React, { useEffect, useState } from 'react'
import { FiCheckCircle, FiGitMerge, FiTarget, FiTrendingUp } from 'react-icons/fi'
import toast from 'react-hot-toast'
import Badge from '@/components/Badge'
import Button from '@/components/Button'
import Card from '@/components/Card'
import { useAuthStore } from '@/context/authStore'
import { coincidenciaService } from '@/services/coincidencia.service'
import { Coincidencia } from '@/types'
import { formatDate } from '@/utils/helpers'

interface CoincidenciaStats {
  totalCoincidencias: number
  coincidenciasConfirmadas: number
  tasaExito: number
}

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
  const { user } = useAuthStore()
  const [coincidencias, setCoincidencias] = useState<Coincidencia[]>([])
  const [stats, setStats] = useState<CoincidenciaStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadData()
  }, [user?.id])

  const loadData = async () => {
    setLoading(true)
    setError(null)
    try {
      const userId = user?.id ?? '1'
      const [coincsData, statsData] = await Promise.all([
        coincidenciaService.getCoincidenciasRecientes(50, userId),
        coincidenciaService.getEstadisticas(userId),
      ])
      setCoincidencias(coincsData)
      setStats(statsData)
    } catch {
      const message = 'Error al cargar coincidencias'
      setError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6 fade-in">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Coincidencias</h1>
        <p className="text-slate-500 text-sm mt-0.5">Motor de matching para reunir mascotas con sus familias</p>
      </div>

      {stats && (
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <Card>
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

          <Card>
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

          <Card>
            <div className="flex items-start justify-between">
              <div>
                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Tasa de Exito</p>
                <p className="text-3xl font-bold text-slate-900 mt-1">{Math.round(stats.tasaExito * 100)}%</p>
              </div>
              <div className="w-10 h-10 bg-accent-50 rounded-xl flex items-center justify-center">
                <FiTrendingUp size={20} className="text-accent-600" />
              </div>
            </div>
          </Card>
        </div>
      )}

      <div className="space-y-4">
        {loading ? (
          <div className="space-y-4">
            {[1, 2, 3].map((item) => (
              <Card key={item} className="animate-pulse">
                <div className="h-32 bg-slate-100 rounded-xl" />
              </Card>
            ))}
          </div>
        ) : error ? (
          <Card className="text-center py-20">
            <p className="text-slate-600 font-semibold text-lg">{error}</p>
            <p className="text-slate-400 text-sm mt-1">Intenta recargar la pagina.</p>
            <Button variant="primary" className="mt-4" onClick={loadData}>Reintentar</Button>
          </Card>
        ) : coincidencias.length === 0 ? (
          <Card className="text-center py-20">
            <div className="text-6xl mb-4">--</div>
            <p className="text-slate-600 font-semibold text-lg">Sin coincidencias pendientes</p>
            <p className="text-slate-400 text-sm mt-1">El backend no reporta coincidencias para este usuario</p>
          </Card>
        ) : (
          coincidencias.map((coincidencia) => (
            <Card key={coincidencia.id} className="border-2 border-accent-200">
              <div className="space-y-4">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-accent-50 rounded-xl flex items-center justify-center">
                      <FiTarget size={20} className="text-accent-600" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-800">Posible coincidencia encontrada</h3>
                      <p className="text-xs text-slate-500">{formatDate(coincidencia.fechaDeteccion)}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge status={coincidencia.estado}>{coincidencia.estado}</Badge>
                    <ScoreRing score={coincidencia.scoreMatching} />
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {[coincidencia.reportePerdido, coincidencia.reporteEncontrado].map((reporte, index) => (
                    <div key={reporte.id} className={`p-4 rounded-xl border ${index === 0 ? 'bg-red-50 border-red-100' : 'bg-emerald-50 border-emerald-100'}`}>
                      <p className={`text-xs font-semibold mb-2 ${index === 0 ? 'text-red-600' : 'text-emerald-600'}`}>
                        {reporte.tipo === 'PERDIDO' ? 'Mascota perdida' : 'Mascota encontrada'}
                      </p>
                      <p className="font-bold text-slate-900">{reporte.mascota.nombre}</p>
                      <p className="text-sm text-slate-600">{reporte.mascota.raza} - {reporte.mascota.color}</p>
                      <p className="text-xs text-slate-500 mt-1">{reporte.ubicacion.ciudad}</p>
                    </div>
                  ))}
                </div>

                {coincidencia.factoresCoincidencia.length > 0 && (
                  <div>
                    <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-2">Factores detectados</p>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                      {coincidencia.factoresCoincidencia.map((factor) => (
                        <div key={`${factor.factor}-${factor.detalle}`} className="flex items-center justify-between bg-slate-50 rounded-lg px-3 py-2">
                          <span className="text-xs text-slate-700">{factor.factor}: {factor.detalle}</span>
                          <span className="text-xs font-bold text-primary-600 ml-2">{Math.round(factor.score * 100)}%</span>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {coincidencia.estado === 'SUGERENCIA' && (
                  <div className="pt-2 border-t border-slate-100">
                    <p className="text-xs text-slate-500">
                      Confirmar o rechazar coincidencias no esta expuesto por el gateway actual.
                    </p>
                  </div>
                )}
              </div>
            </Card>
          ))
        )}
      </div>
    </div>
  )
}

export default CoincidenciasPage
