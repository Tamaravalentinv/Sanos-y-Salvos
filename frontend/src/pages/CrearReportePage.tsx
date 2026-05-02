import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiArrowLeft } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Input from '@/components/Input'
import Select from '@/components/Select'
import Textarea from '@/components/Textarea'
import { reporteService } from '@/services/reporte.service'
import toast from 'react-hot-toast'

const CrearReportePage: React.FC = () => {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState({
    tipo: 'PERDIDO',
    titulo: '',
    descripcion: '',
    nombreMascota: '',
    especie: 'PERRO',
    raza: '',
    color: '',
    tamaño: 'MEDIANO',
    edad: '',
    señasParticulares: '',
    ciudad: '',
    direccion: '',
    latitud: '',
    longitud: '',
  })

  const [errors, setErrors] = useState<Record<string, string>>({})

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const validate = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.titulo.trim()) newErrors.titulo = 'El título es requerido'
    if (!formData.descripcion.trim())
      newErrors.descripcion = 'La descripción es requerida'
    if (!formData.nombreMascota.trim())
      newErrors.nombreMascota = 'El nombre de la mascota es requerido'
    if (!formData.raza.trim()) newErrors.raza = 'La raza es requerida'
    if (!formData.ciudad.trim()) newErrors.ciudad = 'La ciudad es requerida'
    if (!formData.latitud.trim()) newErrors.latitud = 'La latitud es requerida'
    if (!formData.longitud.trim()) newErrors.longitud = 'La longitud es requerida'

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return

    setLoading(true)
    try {
      await reporteService.createReporte({
        titulo: formData.titulo,
        descripcion: formData.descripcion,
        tipo: formData.tipo as 'PERDIDO' | 'ENCONTRADO',
        estado: 'ACTIVO',
        mascota: {
          id: '',
          nombre: formData.nombreMascota,
          especie: formData.especie as 'PERRO' | 'GATO' | 'OTRO',
          raza: formData.raza,
          color: formData.color,
          tamaño: formData.tamaño as 'PEQUEÑO' | 'MEDIANO' | 'GRANDE',
          edad: formData.edad ? parseInt(formData.edad) : undefined,
          señas_particulares: formData.señasParticulares,
        },
        ubicacion: {
          id: '',
          latitud: parseFloat(formData.latitud),
          longitud: parseFloat(formData.longitud),
          direccion: formData.direccion,
          ciudad: formData.ciudad,
          pais: 'España',
        },
        usuarioId: '',
      } as any)

      toast.success('¡Reporte creado exitosamente!')
      navigate('/reportes')
    } catch (error: any) {
      toast.error(
        error.response?.data?.message || 'Error al crear el reporte'
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <button
          onClick={() => navigate('/reportes')}
          className="p-2 hover:bg-gray-100 rounded-lg transition"
        >
          <FiArrowLeft size={24} />
        </button>
        <h1 className="text-3xl font-bold text-gray-900">Crear Nuevo Reporte</h1>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Form */}
        <div className="lg:col-span-2">
          <Card>
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Report Info */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  Información del Reporte
                </h3>
                <div className="space-y-4">
                  <Select
                    label="Tipo de Reporte"
                    name="tipo"
                    value={formData.tipo}
                    onChange={handleChange}
                    options={[
                      { value: 'PERDIDO', label: 'Mascota Perdida' },
                      { value: 'ENCONTRADO', label: 'Mascota Encontrada' },
                    ]}
                  />

                  <Input
                    label="Título"
                    name="titulo"
                    value={formData.titulo}
                    onChange={handleChange}
                    placeholder="Ej: Perro perdido en el parque central"
                    error={errors.titulo}
                  />

                  <Textarea
                    label="Descripción"
                    name="descripcion"
                    value={formData.descripcion}
                    onChange={handleChange}
                    placeholder="Describe la situación con detalle..."
                    error={errors.descripcion}
                    rows={4}
                  />
                </div>
              </div>

              {/* Pet Info */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  Información de la Mascota
                </h3>
                <div className="space-y-4">
                  <Input
                    label="Nombre"
                    name="nombreMascota"
                    value={formData.nombreMascota}
                    onChange={handleChange}
                    placeholder="Ej: Max"
                    error={errors.nombreMascota}
                  />

                  <div className="grid grid-cols-2 gap-4">
                    <Select
                      label="Especie"
                      name="especie"
                      value={formData.especie}
                      onChange={handleChange}
                      options={[
                        { value: 'PERRO', label: 'Perro' },
                        { value: 'GATO', label: 'Gato' },
                        { value: 'OTRO', label: 'Otro' },
                      ]}
                    />

                    <Select
                      label="Tamaño"
                      name="tamaño"
                      value={formData.tamaño}
                      onChange={handleChange}
                      options={[
                        { value: 'PEQUEÑO', label: 'Pequeño' },
                        { value: 'MEDIANO', label: 'Mediano' },
                        { value: 'GRANDE', label: 'Grande' },
                      ]}
                    />
                  </div>

                  <Input
                    label="Raza"
                    name="raza"
                    value={formData.raza}
                    onChange={handleChange}
                    placeholder="Ej: Golden Retriever"
                    error={errors.raza}
                  />

                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Color"
                      name="color"
                      value={formData.color}
                      onChange={handleChange}
                      placeholder="Ej: Marrón"
                    />
                    <Input
                      label="Edad (años)"
                      type="number"
                      name="edad"
                      value={formData.edad}
                      onChange={handleChange}
                      placeholder="Ej: 3"
                    />
                  </div>

                  <Textarea
                    label="Señas Particulares"
                    name="señasParticulares"
                    value={formData.señasParticulares}
                    onChange={handleChange}
                    placeholder="Cicatrices, collar especial, etc."
                    rows={2}
                  />
                </div>
              </div>

              {/* Location Info */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  Información de Ubicación
                </h3>
                <div className="space-y-4">
                  <Input
                    label="Ciudad"
                    name="ciudad"
                    value={formData.ciudad}
                    onChange={handleChange}
                    placeholder="Ej: Madrid"
                    error={errors.ciudad}
                  />

                  <Input
                    label="Dirección (Opcional)"
                    name="direccion"
                    value={formData.direccion}
                    onChange={handleChange}
                    placeholder="Ej: Calle principal 123"
                  />

                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Latitud"
                      type="number"
                      step="0.0001"
                      name="latitud"
                      value={formData.latitud}
                      onChange={handleChange}
                      placeholder="Ej: 40.4168"
                      error={errors.latitud}
                    />
                    <Input
                      label="Longitud"
                      type="number"
                      step="0.0001"
                      name="longitud"
                      value={formData.longitud}
                      onChange={handleChange}
                      placeholder="Ej: -3.7038"
                      error={errors.longitud}
                    />
                  </div>
                </div>
              </div>

              <Button
                type="submit"
                variant="primary"
                size="lg"
                loading={loading}
                className="w-full"
              >
                Crear Reporte
              </Button>
            </form>
          </Card>
        </div>

        {/* Info Panel */}
        <div>
          <Card className="bg-blue-50 border-blue-200">
            <h3 className="font-semibold text-blue-900 mb-4">Consejos</h3>
            <ul className="space-y-3 text-sm text-blue-800">
              <li className="flex gap-2">
                <span>📸</span>
                <span>Incluye fotos claras de tu mascota</span>
              </li>
              <li className="flex gap-2">
                <span>📝</span>
                <span>Se específico en la descripción</span>
              </li>
              <li className="flex gap-2">
                <span>📍</span>
                <span>Incluye ubicación exacta</span>
              </li>
              <li className="flex gap-2">
                <span>⚡</span>
                <span>Comparte con amigos y familia</span>
              </li>
            </ul>
          </Card>
        </div>
      </div>
    </div>
  )
}

export default CrearReportePage
