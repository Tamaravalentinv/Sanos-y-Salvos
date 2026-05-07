import React, { useEffect, useState } from 'react'
import { Link, useSearchParams, useNavigate } from 'react-router-dom'
import { FiPlus, FiFilter, FiX } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import Input from '@/components/Input'
import Select from '@/components/Select'
import { reporteService } from '@/services/reporte.service'
import { Reporte } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const TIPO_OPS   = [{ value: 'PERDIDO', label: '🔴 Perdido' }, { value: 'ENCONTRADO', label: '🟡 Encontrado' }]
const ESTADO_OPS = [{ value: 'ACTIVO', label: 'Activo' }, { value: 'RESUELTO', label: 'Resuelto' }, { value: 'CANCELADO', label: 'Cancelado' }]

const EspecieEmoji = ({ especie }: { especie?: string }) => {
  if (especie === 'PERRO') return <span>🐕</span>
  if (especie === 'GATO') return <span>🐈</span>
  return <span>🐾</span>
}

const ReportesPage: React.FC = () => {
  const [searchParams] = useSearchParams()
  const [reportes, setReportes] = useState<Reporte[]>([])
  const [loading, setLoading] = useState(true)
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(parseInt(searchParams.get('page') || '0'))
  const navigate = useNavigate()
  const [showFilters, setShowFilters] = useState(false)
  const [filters, setFilters] = useState({ tipo: '', estado: '', ciudad: '' })

  useEffect(() => { loadReportes() }, [page, filters])

  const loadReportes = async () => {
    setLoading(true)
    try {
      const res = await reporteService.getAllReportes({ ...filters, page, size: 12 })
      setReportes(res.content)
      setTotal(res.totalElements)
    } catch {
      toast.error('Error al cargar reportes')
    } finally {
      setLoading(false)
    }
  }

  const clearFilters = () => { setFilters({ tipo: '', estado: '', ciudad: '' }); setPage(0) }
  const hasFilters = filters.tipo || filters.estado || filters.ciudad

  return (
    <div className="space-y-5 fade-in">
      {/* Header */}
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Reportes</h1>
          <p className="text-slate-500 text-sm mt-0.5">
            {loading ? '…' : `${total} reportes encontrados`}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant={showFilters ? 'primary' : 'secondary'}
            size="sm"
            onClick={() => setShowFilters(v => !v)}
          >
            <FiFilter size={14} /> Filtros {hasFilters && '•'}
          </Button>
          <Link to="/reportes/crear">
            <Button variant="accent" size="sm"><FiPlus size={14} /> Nuevo Reporte</Button>
          </Link>
        </div>
      </div>

      {/* Filters panel */}
      {showFilters && (
        <Card className="border-primary-100 bg-primary-50/50">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <Select label="Tipo" value={filters.tipo}
              onChange={e => { setFilters(p => ({ ...p, tipo: e.target.value })); setPage(0) }}
              options={TIPO_OPS} />
            <Select label="Estado" value={filters.estado}
              onChange={e => { setFilters(p => ({ ...p, estado: e.target.value })); setPage(0) }}
              options={ESTADO_OPS} />
            <Input label="Ciudad" value={filters.ciudad}
              onChange={e => { setFilters(p => ({ ...p, ciudad: e.target.value })); setPage(0) }}
              placeholder="Ej: Santiago" />
          </div>
          {hasFilters && (
            <button onClick={clearFilters}
              className="mt-3 inline-flex items-center gap-1 text-xs text-slate-500 hover:text-red-500 transition">
              <FiX size={12} /> Limpiar filtros
            </button>
          )}
        </Card>
      )}

      {/* Content */}
      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {Array.from({ length: 6 }).map((_, i) => (
            <Card key={i} className="animate-pulse">
              <div className="h-40 bg-slate-100 rounded-xl mb-4" />
              <div className="h-4 bg-slate-100 rounded w-2/3 mb-2" />
              <div className="h-3 bg-slate-100 rounded w-1/2" />
            </Card>
          ))}
        </div>
      ) : reportes.length === 0 ? (
        <div className="text-center py-20">
          <div className="text-6xl mb-4">🔍</div>
          <p className="text-slate-600 font-semibold text-lg">Sin resultados</p>
          <p className="text-slate-400 text-sm mt-1">Intenta con otros filtros o crea el primer reporte</p>
          <Link to="/reportes/crear" className="mt-6 inline-block">
            <Button variant="primary"><FiPlus size={16} /> Crear Reporte</Button>
          </Link>
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
            {reportes.map(r => (
              <Card
                key={r.id}
                hover
                className="flex flex-col cursor-pointer"
                onClick={() => navigate(`/reportes/${r.id}`)}
              >
                {/* Image or gradient placeholder */}
                <div className="relative mb-4 overflow-hidden rounded-xl">
                  {r.mascota?.fotografia ? (
                    <img src={r.mascota.fotografia} alt={r.mascota.nombre}
                      className="w-full h-44 object-cover" />
                  ) : (
                    <div className="w-full h-44 bg-gradient-to-br from-primary-50 to-primary-100
                                    flex items-center justify-center">
                      <span className="text-6xl opacity-50"><EspecieEmoji especie={r.mascota?.especie} /></span>
                    </div>
                  )}
                  <div className="absolute top-2 right-2 flex gap-1">
                    <Badge status={r.tipo}>{r.tipo}</Badge>
                  </div>
                </div>

                <div className="flex-1 flex flex-col">
                  <div className="flex items-start justify-between mb-2">
                    <div>
                      <h3 className="font-bold text-slate-900">{r.mascota?.nombre ?? 'Sin nombre'}</h3>
                      <p className="text-xs text-slate-500">{r.mascota?.raza} · {r.mascota?.tamaño}</p>
                    </div>
                    <Badge status={r.estado}>{r.estado}</Badge>
                  </div>

                  {r.mascota?.color && (
                    <p className="text-xs text-slate-600 mb-1">🎨 {r.mascota.color}</p>
                  )}
                  <p className="text-xs text-slate-500 mb-1">📍 {r.ubicacion?.ciudad ?? '—'}</p>
                  <p className="text-xs text-slate-400 mb-3">📅 {formatDate(r.fechaReporte)}</p>

                  {r.descripcion && (
                    <p className="text-xs text-slate-600 line-clamp-2">{r.descripcion}</p>
                  )}
                </div>
              </Card>
            ))}
          </div>

          {/* Pagination */}
          <div className="flex items-center justify-center gap-3 pt-2">
            <Button variant="secondary" size="sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>
              ← Anterior
            </Button>
            <span className="text-sm text-slate-500 font-medium">Página {page + 1}</span>
            <Button variant="secondary" size="sm" disabled={reportes.length < 12} onClick={() => setPage(p => p + 1)}>
              Siguiente →
            </Button>
          </div>
        </>
      )}
    </div>
  )
}

export default ReportesPage
