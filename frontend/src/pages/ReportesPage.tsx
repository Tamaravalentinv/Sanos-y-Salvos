import React, { useEffect, useState } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import { FiFilter, FiSearch, FiPlus } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Badge from '@/components/Badge'
import Input from '@/components/Input'
import Select from '@/components/Select'
import { reporteService } from '@/services/reporte.service'
import { Reporte } from '@/types'
import { formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const ReportesPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams()
  const [reportes, setReportes] = useState<Reporte[]>([])
  const [loading, setLoading] = useState(true)
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(parseInt(searchParams.get('page') || '0'))
  
  const [filters, setFilters] = useState({
    tipo: searchParams.get('tipo') || '',
    estado: searchParams.get('estado') || '',
    ciudad: searchParams.get('ciudad') || '',
  })

  useEffect(() => {
    loadReportes()
  }, [page, filters])

  const loadReportes = async () => {
    setLoading(true)
    try {
      const result = await reporteService.getAllReportes({
        ...filters,
        page,
        size: 10,
      })
      setReportes(result.content)
      setTotal(result.totalElements)
    } catch (error) {
      toast.error('Error al cargar reportes')
    } finally {
      setLoading(false)
    }
  }

  const handleFilterChange = (field: string, value: string) => {
    setFilters((prev) => ({ ...prev, [field]: value }))
    setPage(0)
  }

  const clearFilters = () => {
    setFilters({ tipo: '', estado: '', ciudad: '' })
    setPage(0)
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Reportes</h1>
        <Link to="/reportes/crear">
          <Button variant="primary" size="lg">
            <FiPlus size={20} />
            Nuevo Reporte
          </Button>
        </Link>
      </div>

      {/* Filters */}
      <Card>
        <div className="space-y-4">
          <h3 className="font-semibold text-gray-900 flex items-center gap-2">
            <FiFilter size={20} /> Filtros
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Select
              label="Tipo"
              value={filters.tipo}
              onChange={(e) => handleFilterChange('tipo', e.target.value)}
              options={[
                { value: 'PERDIDO', label: 'Perdido' },
                { value: 'ENCONTRADO', label: 'Encontrado' },
              ]}
            />
            <Select
              label="Estado"
              value={filters.estado}
              onChange={(e) => handleFilterChange('estado', e.target.value)}
              options={[
                { value: 'ACTIVO', label: 'Activo' },
                { value: 'RESUELTO', label: 'Resuelto' },
                { value: 'CANCELADO', label: 'Cancelado' },
              ]}
            />
            <Input
              label="Ciudad"
              value={filters.ciudad}
              onChange={(e) => handleFilterChange('ciudad', e.target.value)}
              placeholder="Buscar ciudad"
            />
          </div>
          <Button
            variant="secondary"
            onClick={clearFilters}
            className="w-full md:w-auto"
          >
            Limpiar Filtros
          </Button>
        </div>
      </Card>

      {/* Reports Grid */}
      <div className="space-y-4">
        {loading ? (
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto mb-4"></div>
              <p className="text-gray-600">Cargando reportes...</p>
            </div>
          </div>
        ) : reportes.length > 0 ? (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {reportes.map((reporte) => (
                <Card key={reporte.id} hover>
                  {reporte.mascota.fotografia && (
                    <img
                      src={reporte.mascota.fotografia}
                      alt={reporte.mascota.nombre}
                      className="w-full h-48 object-cover rounded-lg mb-4"
                    />
                  )}
                  <div className="space-y-3">
                    <div className="flex items-start justify-between">
                      <div>
                        <h3 className="text-lg font-bold text-gray-900">
                          {reporte.mascota.nombre}
                        </h3>
                        <p className="text-sm text-gray-600">
                          {reporte.mascota.raza} • {reporte.mascota.tamaño}
                        </p>
                      </div>
                      <Badge status={reporte.tipo}>{reporte.tipo}</Badge>
                    </div>

                    <div className="flex gap-2">
                      <Badge status={reporte.estado}>{reporte.estado}</Badge>
                    </div>

                    <div className="text-sm text-gray-600 space-y-1">
                      <p>📍 {reporte.ubicacion.ciudad}</p>
                      <p>📅 {formatDate(reporte.fechaReporte)}</p>
                      {reporte.mascota.color && (
                        <p>🎨 {reporte.mascota.color}</p>
                      )}
                    </div>

                    <p className="text-sm text-gray-600 line-clamp-2">
                      {reporte.descripcion}
                    </p>

                    <Link to={`/reportes/${reporte.id}`}>
                      <Button variant="primary" size="sm" className="w-full">
                        Ver Detalles
                      </Button>
                    </Link>
                  </div>
                </Card>
              ))}
            </div>

            {/* Pagination */}
            <div className="flex items-center justify-center gap-2 mt-6">
              <Button
                variant="secondary"
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
              >
                Anterior
              </Button>
              <span className="text-gray-600">
                Página {page + 1}
              </span>
              <Button
                variant="secondary"
                disabled={reportes.length < 10}
                onClick={() => setPage(page + 1)}
              >
                Siguiente
              </Button>
            </div>
          </>
        ) : (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">No hay reportes con estos filtros</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default ReportesPage
