import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { FiPlus, FiTrendingUp } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { dashboardService } from '@/services/dashboard.service'
import { reporteService } from '@/services/reporte.service'
import { EstadisticasGlobales, Reporte } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const DashboardPage: React.FC = () => {
  const [stats, setStats] = useState<EstadisticasGlobales | null>(null)
  const [reportesRecientes, setReportesRecientes] = useState<Reporte[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [statsData, reportesData] = await Promise.all([
        dashboardService.getEstadisticasGlobales(),
        reporteService.getAllReportes({ page: 0, size: 5 }),
      ])
      setStats(statsData)
      setReportesRecientes(reportesData.content)
    } catch (error) {
      toast.error('Error al cargar el dashboard')
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="text-gray-600 mt-1">
            Bienvenido a Sanos y Salvos - Recupera tu mascota
          </p>
        </div>
        <Link to="/reportes/crear">
          <Button variant="primary" size="lg">
            <FiPlus size={20} />
            Crear Reporte
          </Button>
        </Link>
      </div>

      {/* Stats Grid */}
      {stats && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="bg-gradient-to-br from-blue-50 to-blue-100">
            <div className="space-y-2">
              <p className="text-blue-600 text-sm font-semibold">Total Reportes</p>
              <p className="text-3xl font-bold text-blue-900">{stats.totalReportes}</p>
              <p className="text-blue-600 text-xs">
                {stats.reportesActivos} activos
              </p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-green-50 to-green-100">
            <div className="space-y-2">
              <p className="text-green-600 text-sm font-semibold">Resueltos</p>
              <p className="text-3xl font-bold text-green-900">{stats.reportesResueltos}</p>
              <p className="text-green-600 text-xs">
                {Math.round(stats.tasaResolucion * 100)}% tasa de éxito
              </p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-purple-50 to-purple-100">
            <div className="space-y-2">
              <p className="text-purple-600 text-sm font-semibold">Coincidencias</p>
              <p className="text-3xl font-bold text-purple-900">
                {stats.coincidenciasDetectadas}
              </p>
              <p className="text-purple-600 text-xs">Detectadas</p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-orange-50 to-orange-100">
            <div className="space-y-2">
              <p className="text-orange-600 text-sm font-semibold">Usuarios Activos</p>
              <p className="text-3xl font-bold text-orange-900">{stats.usuariosActivos}</p>
              <p className="text-orange-600 text-xs">En la comunidad</p>
            </div>
          </Card>
        </div>
      )}

      {/* Quick Actions */}
      <Card>
        <h2 className="text-xl font-bold text-gray-900 mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link to="/reportes/crear" className="block">
            <div className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition cursor-pointer text-center">
              <p className="text-2xl mb-2">📝</p>
              <p className="font-semibold text-gray-900">Crear Reporte</p>
              <p className="text-sm text-gray-600">Reporta una mascota perdida</p>
            </div>
          </Link>

          <Link to="/reportes" className="block">
            <div className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition cursor-pointer text-center">
              <p className="text-2xl mb-2">🔍</p>
              <p className="font-semibold text-gray-900">Ver Reportes</p>
              <p className="text-sm text-gray-600">Busca mascotas cercanas</p>
            </div>
          </Link>

          <Link to="/mapa" className="block">
            <div className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition cursor-pointer text-center">
              <p className="text-2xl mb-2">🗺️</p>
              <p className="font-semibold text-gray-900">Ver Mapa</p>
              <p className="text-sm text-gray-600">Zonas de incidencia</p>
            </div>
          </Link>
        </div>
      </Card>

      {/* Recent Reports */}
      <Card>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-bold text-gray-900">Reportes Recientes</h2>
          <Link to="/reportes">
            <Button variant="secondary" size="sm">
              Ver Todos
            </Button>
          </Link>
        </div>

        <div className="space-y-3">
          {reportesRecientes.length > 0 ? (
            reportesRecientes.map((reporte) => (
              <div
                key={reporte.id}
                className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition"
              >
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <p className="font-semibold text-gray-900">
                        {reporte.mascota.nombre} - {reporte.mascota.especie}
                      </p>
                      <Badge status={reporte.estado}>{reporte.estado}</Badge>
                      <Badge status={reporte.tipo}>{reporte.tipo}</Badge>
                    </div>
                    <p className="text-sm text-gray-600">
                      {reporte.mascota.raza} • {reporte.mascota.tamaño}
                    </p>
                    <p className="text-xs text-gray-500 mt-1">
                      📍 {reporte.ubicacion.ciudad} • {formatDate(reporte.fechaReporte)}
                    </p>
                  </div>
                  <Link to={`/reportes/${reporte.id}`}>
                    <Button variant="secondary" size="sm">
                      Ver
                    </Button>
                  </Link>
                </div>
              </div>
            ))
          ) : (
            <div className="text-center py-8">
              <p className="text-gray-500">No hay reportes aún</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  )
}

export default DashboardPage
