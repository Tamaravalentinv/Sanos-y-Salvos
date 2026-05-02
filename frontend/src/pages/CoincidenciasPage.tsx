import React, { useEffect, useState } from 'react'
import { FiCheckCircle, FiXCircle } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { coincidenciaService } from '@/services/coincidencia.service'
import { Coincidencia } from '@/types'
import { formatDate, calculateScoreColor } from '@/utils/helpers'
import toast from 'react-hot-toast'

const CoincidenciasPage: React.FC = () => {
  const [coincidencias, setCoincidenias] = useState<Coincidencia[]>([])
  const [stats, setStats] = useState<any>(null)
  const [loading, setLoading] = useState(true)
  const [actionLoading, setActionLoading] = useState<string | null>(null)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [coincsData, statsData] = await Promise.all([
        coincidenciaService.getCoincidendasRecientes(50),
        coincidenciaService.getEstadisticas(),
      ])
      setCoincidenias(coincsData)
      setStats(statsData)
    } catch (error) {
      toast.error('Error al cargar coincidencias')
    } finally {
      setLoading(false)
    }
  }

  const handleConfirm = async (coincidenciaId: string) => {
    setActionLoading(coincidenciaId)
    try {
      const updated = await coincidenciaService.confirmarCoincidencia(
        coincidenciaId
      )
      setCoincidenias((prev) =>
        prev.map((c) => (c.id === coincidenciaId ? updated : c))
      )
      toast.success('¡Coincidencia confirmada!')
    } catch (error) {
      toast.error('Error al confirmar coincidencia')
    } finally {
      setActionLoading(null)
    }
  }

  const handleReject = async (coincidenciaId: string) => {
    setActionLoading(coincidenciaId)
    try {
      await coincidenciaService.rechazarCoincidencia(coincidenciaId)
      setCoincidenias((prev) => prev.filter((c) => c.id !== coincidenciaId))
      toast.success('Coincidencia rechazada')
    } catch (error) {
      toast.error('Error al rechazar coincidencia')
    } finally {
      setActionLoading(null)
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Coincidencias</h1>
        <p className="text-gray-600 mt-1">
          Revisa las coincidencias detectadas por nuestro motor de matching
        </p>
      </div>

      {/* Stats */}
      {stats && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-50 to-blue-100">
            <div className="space-y-2">
              <p className="text-blue-600 text-sm font-semibold">
                Total de Coincidencias
              </p>
              <p className="text-3xl font-bold text-blue-900">
                {stats.totalCoincidencias}
              </p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-green-50 to-green-100">
            <div className="space-y-2">
              <p className="text-green-600 text-sm font-semibold">Confirmadas</p>
              <p className="text-3xl font-bold text-green-900">
                {stats.coincidenciasConfirmadas}
              </p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-purple-50 to-purple-100">
            <div className="space-y-2">
              <p className="text-purple-600 text-sm font-semibold">Tasa de Éxito</p>
              <p className="text-3xl font-bold text-purple-900">
                {Math.round(stats.tasaExito * 100)}%
              </p>
            </div>
          </Card>
        </div>
      )}

      {/* Matches List */}
      <div className="space-y-4">
        {loading ? (
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto mb-4"></div>
              <p className="text-gray-600">Cargando coincidencias...</p>
            </div>
          </div>
        ) : coincidencias.length > 0 ? (
          coincidencias.map((coincidencia) => {
            const scorePercent = Math.round(coincidencia.scoreMatching * 100)
            const scoreClass = calculateScoreColor(coincidencia.scoreMatching)

            return (
              <Card key={coincidencia.id} className="border-2">
                <div className="space-y-4">
                  {/* Header */}
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <h3 className="text-lg font-bold text-gray-900 mb-2">
                        ¡Posible coincidencia encontrada!
                      </h3>
                      <div className="flex gap-2 flex-wrap">
                        <Badge status={coincidencia.estado}>
                          {coincidencia.estado}
                        </Badge>
                        <span className={`px-3 py-1 rounded-full text-sm font-semibold ${scoreClass}`}>
                          Similitud: {scorePercent}%
                        </span>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-sm text-gray-600">
                        {formatDate(coincidencia.fechaDeteccion)}
                      </p>
                    </div>
                  </div>

                  {/* Reports Comparison */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="p-4 bg-gray-50 rounded-lg">
                      <p className="font-semibold text-gray-900 mb-2">
                        {coincidencia.reportePerdido.tipo === 'PERDIDO'
                          ? '🔴 Mascota Perdida'
                          : '🟢 Mascota Encontrada'}
                      </p>
                      <p className="text-lg font-bold text-gray-900">
                        {coincidencia.reportePerdido.mascota.nombre}
                      </p>
                      <p className="text-sm text-gray-600">
                        {coincidencia.reportePerdido.mascota.raza} •{' '}
                        {coincidencia.reportePerdido.mascota.color}
                      </p>
                      <p className="text-xs text-gray-500 mt-2">
                        📍 {coincidencia.reportePerdido.ubicacion.ciudad}
                      </p>
                    </div>

                    <div className="p-4 bg-gray-50 rounded-lg">
                      <p className="font-semibold text-gray-900 mb-2">
                        {coincidencia.reporteEncontrado.tipo === 'PERDIDO'
                          ? '🔴 Mascota Perdida'
                          : '🟢 Mascota Encontrada'}
                      </p>
                      <p className="text-lg font-bold text-gray-900">
                        {coincidencia.reporteEncontrado.mascota.nombre}
                      </p>
                      <p className="text-sm text-gray-600">
                        {coincidencia.reporteEncontrado.mascota.raza} •{' '}
                        {coincidencia.reporteEncontrado.mascota.color}
                      </p>
                      <p className="text-xs text-gray-500 mt-2">
                        📍 {coincidencia.reporteEncontrado.ubicacion.ciudad}
                      </p>
                    </div>
                  </div>

                  {/* Factors */}
                  <div>
                    <p className="font-semibold text-gray-900 mb-2">
                      Factores de Coincidencia
                    </p>
                    <div className="space-y-2">
                      {coincidencia.factoresCoincidencia.map((factor, idx) => (
                        <div
                          key={idx}
                          className="flex items-center justify-between p-2 bg-gray-50 rounded"
                        >
                          <span className="text-sm text-gray-700">
                            {factor.factor}: {factor.detalle}
                          </span>
                          <span className="text-sm font-semibold text-primary-600">
                            {Math.round(factor.score * 100)}%
                          </span>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Actions */}
                  {coincidencia.estado === 'SUGERENCIA' && (
                    <div className="flex gap-2">
                      <Button
                        variant="success"
                        size="sm"
                        onClick={() => handleConfirm(coincidencia.id)}
                        loading={actionLoading === coincidencia.id}
                        className="flex-1"
                      >
                        <FiCheckCircle size={18} />
                        Confirmar Coincidencia
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleReject(coincidencia.id)}
                        loading={actionLoading === coincidencia.id}
                        className="flex-1"
                      >
                        <FiXCircle size={18} />
                        Rechazar
                      </Button>
                    </div>
                  )}
                </div>
              </Card>
            )
          })
        ) : (
          <Card>
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">
                No hay coincidencias pendientes de revisión
              </p>
            </div>
          </Card>
        )}
      </div>
    </div>
  )
}

export default CoincidenciasPage
