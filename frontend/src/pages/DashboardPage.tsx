import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { FiPlus, FiFileText, FiGitMerge, FiMapPin, FiTrendingUp, FiAlertCircle } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import { dashboardService } from '@/services/dashboard.service'
import { reporteService } from '@/services/reporte.service'
import { EstadisticasGlobales, Reporte } from '@/types'
import { formatDate } from '@/utils/helpers'
import { useAuthStore } from '@/context/authStore'
import toast from 'react-hot-toast'

const StatCard = ({
  icon, label, value, sub, color,
}: { icon: React.ReactNode; label: string; value: string | number; sub?: string; color: string }) => (
  <Card className="relative overflow-hidden">
    <div className={`absolute -top-4 -right-4 w-24 h-24 rounded-full opacity-10 ${color}`} />
    <div className="flex items-start justify-between">
      <div>
        <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">{label}</p>
        <p className="text-3xl font-bold text-slate-900 mt-1">{value}</p>
        {sub && <p className="text-xs text-slate-500 mt-1">{sub}</p>}
      </div>
      <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${color} bg-opacity-15`}>
        {icon}
      </div>
    </div>
  </Card>
)

const Skeleton = () => (
  <div className="animate-pulse space-y-3">
    <div className="h-6 bg-slate-100 rounded-xl w-1/3" />
    <div className="h-4 bg-slate-100 rounded-xl w-1/2" />
  </div>
)

const DashboardPage: React.FC = () => {
  const { user } = useAuthStore()
  const [stats, setStats] = useState<EstadisticasGlobales | null>(null)
  const [reportes, setReportes] = useState<Reporte[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => { loadData() }, [])

  const loadData = async () => {
    setLoading(true)
    setError(null)
    try {
      const [statsData, reportesData] = await Promise.all([
        dashboardService.getEstadisticasGlobales().catch(() => null),
        reporteService.getAllReportes({ page: 0, size: 5 }).catch(() => ({ content: [], totalElements: 0, totalPages: 0 })),
      ])
      if (statsData) setStats(statsData)
      setReportes(reportesData.content)
    } catch {
      const message = 'Error al cargar el dashboard'
      setError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }

  const hora = new Date().getHours()
  const saludo = hora < 12 ? 'Buenos días' : hora < 18 ? 'Buenas tardes' : 'Buenas noches'

  return (
    <div className="space-y-6 fade-in">
      {/* Welcome banner */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-800 rounded-2xl p-6 text-white relative overflow-hidden">
        <div className="absolute right-0 top-0 text-[8rem] leading-none opacity-10 select-none">🐾</div>
        <div className="relative z-10">
          <p className="text-primary-200 text-sm font-medium">{saludo},</p>
          <h1 className="text-2xl font-bold mt-0.5">{user?.nombre} {user?.apellido} 👋</h1>
          <p className="text-primary-200 text-sm mt-2 max-w-sm">
            Bienvenido a Sanos y Salvos. Ayudamos a reunir familias con sus mascotas.
          </p>
          <Link to="/reportes/crear" className="mt-4 inline-block">
            <Button variant="accent" size="md">
              <FiPlus size={16} /> Crear nuevo reporte
            </Button>
          </Link>
        </div>
      </div>

      {/* Stats */}
      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => <Card key={i}><Skeleton /></Card>)}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <StatCard
            icon={<FiFileText size={20} className="text-primary-600" />}
            label="Total Reportes" value={stats?.totalReportes ?? reportes.length}
            sub={`${stats?.reportesActivos ?? '—'} activos`}
            color="bg-primary-500"
          />
          <StatCard
            icon={<FiTrendingUp size={20} className="text-emerald-600" />}
            label="Resueltos" value={stats?.reportesResueltos ?? '—'}
            sub={stats ? `${Math.round(stats.tasaResolucion * 100)}% éxito` : undefined}
            color="bg-emerald-500"
          />
          <StatCard
            icon={<FiGitMerge size={20} className="text-accent-600" />}
            label="Coincidencias" value={stats?.coincidenciasDetectadas ?? '—'}
            sub="detectadas" color="bg-accent-500"
          />
          <StatCard
            icon={<FiMapPin size={20} className="text-violet-600" />}
            label="Usuarios Activos" value={stats?.usuariosActivos ?? '—'}
            sub="en la comunidad" color="bg-violet-500"
          />
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent reports */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="font-bold text-slate-800 text-lg">Reportes Recientes</h2>
            <Link to="/reportes">
              <Button variant="ghost" size="sm">Ver todos →</Button>
            </Link>
          </div>

          {loading ? (
            <Card><Skeleton /></Card>
          ) : error ? (
            <Card className="text-center py-12">
              <p className="text-slate-600 font-semibold">{error}</p>
              <Button variant="primary" size="sm" className="mt-4" onClick={loadData}>Reintentar</Button>
            </Card>
          ) : reportes.length === 0 ? (
            <Card className="text-center py-12">
              <div className="text-5xl mb-3">🔍</div>
              <p className="text-slate-500 font-medium">No hay reportes aún</p>
              <p className="text-slate-400 text-sm mt-1">¡Sé el primero en crear uno!</p>
              <Link to="/reportes/crear" className="mt-4 inline-block">
                <Button variant="primary" size="sm"><FiPlus size={14} /> Crear reporte</Button>
              </Link>
            </Card>
          ) : (
            <div className="space-y-3">
              {reportes.map(r => (
                <Card key={r.id} hover>
                  <div className="flex items-center gap-4">
                    <div className="w-12 h-12 rounded-xl bg-primary-50 flex items-center justify-center text-2xl flex-shrink-0">
                      {r.mascota?.especie === 'PERRO' ? '🐕' : r.mascota?.especie === 'GATO' ? '🐈' : '🐾'}
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 flex-wrap">
                        <p className="font-semibold text-slate-800 truncate">
                          {r.mascota?.nombre ?? 'Sin nombre'} — {r.mascota?.especie}
                        </p>
                        <Badge status={r.tipo}>{r.tipo}</Badge>
                        <Badge status={r.estado}>{r.estado}</Badge>
                      </div>
                      <p className="text-xs text-slate-500 mt-0.5 truncate">
                        📍 {r.ubicacion?.ciudad ?? '—'} · {formatDate(r.fechaReporte)}
                      </p>
                    </div>
                    <Link to={`/reportes/${r.id}`} className="flex-shrink-0">
                      <Button variant="secondary" size="sm">Ver</Button>
                    </Link>
                  </div>
                </Card>
              ))}
            </div>
          )}
        </div>

        {/* Quick actions */}
        <div className="space-y-4">
          <h2 className="font-bold text-slate-800 text-lg">Acciones Rápidas</h2>

          {[
            { to: '/reportes/crear', icon: '📝', title: 'Crear Reporte', desc: 'Reporta una mascota perdida o encontrada', color: 'border-primary-200 hover:border-primary-400 hover:bg-primary-50' },
            { to: '/coincidencias',  icon: '🔗', title: 'Coincidencias', desc: 'Ver mascotas que podrían ser la tuya',    color: 'border-accent-200 hover:border-accent-400 hover:bg-accent-50' },
            { to: '/mapa',           icon: '🗺️', title: 'Mapa',         desc: 'Zonas de mayor incidencia',              color: 'border-emerald-200 hover:border-emerald-400 hover:bg-emerald-50' },
          ].map(item => (
            <Link key={item.to} to={item.to}>
              <Card className={`border-2 transition-all duration-150 cursor-pointer ${item.color}`} padding="sm">
                <div className="flex items-center gap-3">
                  <span className="text-2xl">{item.icon}</span>
                  <div>
                    <p className="font-semibold text-slate-800 text-sm">{item.title}</p>
                    <p className="text-xs text-slate-500">{item.desc}</p>
                  </div>
                </div>
              </Card>
            </Link>
          ))}

          {/* Alert tip */}
          <Card className="bg-amber-50 border-amber-200 border">
            <div className="flex gap-3">
              <FiAlertCircle size={18} className="text-amber-600 flex-shrink-0 mt-0.5" />
              <div>
                <p className="text-xs font-semibold text-amber-800">Tip importante</p>
                <p className="text-xs text-amber-700 mt-0.5">
                  Añade fotos claras y describe señas particulares para aumentar las posibilidades de recuperación.
                </p>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}

export default DashboardPage
