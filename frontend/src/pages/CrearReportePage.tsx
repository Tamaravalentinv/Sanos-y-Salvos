import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiArrowLeft, FiMapPin, FiInfo } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Input from '@/components/Input'
import Select from '@/components/Select'
import Textarea from '@/components/Textarea'
import { reporteService } from '@/services/reporte.service'
import { useAuthStore } from '@/context/authStore'
import toast from 'react-hot-toast'

const ESPECIE_OPS = [
  { value: 'PERRO', label: '🐕 Perro' },
  { value: 'GATO',  label: '🐈 Gato' },
  { value: 'OTRO',  label: '🐾 Otro' },
]
const TAMAÑO_OPS = [
  { value: 'PEQUEÑO', label: 'Pequeño (< 10 kg)' },
  { value: 'MEDIANO', label: 'Mediano (10–25 kg)' },
  { value: 'GRANDE',  label: 'Grande (> 25 kg)' },
]

const STEP_LABELS = ['Tipo de reporte', 'Mascota', 'Ubicación']

const CrearReportePage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [step, setStep] = useState(0)

  const [form, setForm] = useState({
    tipo: 'PERDIDO',
    descripcion: '',
    nombreMascota: '', especie: 'PERRO', raza: '',
    color: '', tamaño: 'MEDIANO', edad: '', señasParticulares: '',
    ciudad: '', direccion: '', latitud: '', longitud: '',
  })
  const [errs, setErrs] = useState<Record<string, string>>({})

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
    if (errs[name]) setErrs(prev => ({ ...prev, [name]: '' }))
  }

  const validateStep = (s: number) => {
    const e: Record<string, string> = {}
    if (s === 0 && !form.descripcion.trim()) e.descripcion = 'Requerida'
    if (s === 1) {
      if (!form.nombreMascota.trim()) e.nombreMascota = 'Requerido'
      if (!form.raza.trim()) e.raza = 'Requerida'
    }
    if (s === 2) {
      if (!form.ciudad.trim()) e.ciudad = 'Requerida'
    }
    setErrs(e)
    return Object.keys(e).length === 0
  }

  const nextStep = () => { if (validateStep(step)) setStep(s => s + 1) }
  const prevStep = () => setStep(s => s - 1)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validateStep(2)) return
    setLoading(true)
    try {
      await reporteService.createReporte({
        titulo: `${form.tipo === 'PERDIDO' ? 'Perdido' : 'Encontrado'}: ${form.nombreMascota}`,
        descripcion: form.descripcion,
        tipo: form.tipo as 'PERDIDO' | 'ENCONTRADO',
        estado: 'ACTIVO',
        mascota: {
          id: '', nombre: form.nombreMascota,
          especie: form.especie as 'PERRO' | 'GATO' | 'OTRO',
          raza: form.raza, color: form.color,
          tamaño: form.tamaño as 'PEQUEÑO' | 'MEDIANO' | 'GRANDE',
          edad: form.edad ? parseInt(form.edad) : undefined,
          señas_particulares: form.señasParticulares,
        },
        ubicacion: {
          id: '',
          latitud: parseFloat(form.latitud) || 0,
          longitud: parseFloat(form.longitud) || 0,
          direccion: form.direccion,
          ciudad: form.ciudad,
          pais: 'Chile',
        },
        usuarioId: user?.id ?? '',
      } as any)
      toast.success('¡Reporte creado exitosamente! 🐾')
      navigate('/reportes')
    } catch (err: any) {
      toast.error(err?.response?.data?.message || 'Error al crear el reporte')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6 fade-in">
      {/* Header */}
      <div className="flex items-center gap-3">
        <Button variant="ghost" size="sm" onClick={() => navigate('/reportes')}>
          <FiArrowLeft size={16} /> Volver
        </Button>
        <div>
          <h1 className="text-xl font-bold text-slate-900">Crear Reporte</h1>
          <p className="text-slate-500 text-sm">{STEP_LABELS[step]} · Paso {step + 1} de 3</p>
        </div>
      </div>

      {/* Progress bar */}
      <div className="h-1.5 bg-slate-100 rounded-full overflow-hidden">
        <div
          className="h-full bg-gradient-to-r from-primary-500 to-accent-500 rounded-full transition-all duration-300"
          style={{ width: `${((step + 1) / 3) * 100}%` }}
        />
      </div>

      <form onSubmit={handleSubmit}>
        {/* ── Step 0: Tipo ── */}
        {step === 0 && (
          <Card className="space-y-5">
            <h2 className="font-bold text-slate-800 text-base">¿Qué pasó?</h2>

            <div className="grid grid-cols-2 gap-3">
              {[
                { value: 'PERDIDO',    emoji: '😟', title: 'Mascota Perdida',    desc: 'No encuentro a mi mascota' },
                { value: 'ENCONTRADO', emoji: '😊', title: 'Mascota Encontrada', desc: 'Encontré una mascota' },
              ].map(opt => (
                <button
                  key={opt.value} type="button"
                  onClick={() => setForm(p => ({ ...p, tipo: opt.value }))}
                  className={`
                    flex flex-col items-center gap-2 p-5 rounded-2xl border-2 transition-all
                    ${form.tipo === opt.value
                      ? 'border-primary-500 bg-primary-50 shadow-md'
                      : 'border-slate-200 hover:border-slate-300 bg-white'}
                  `}
                >
                  <span className="text-4xl">{opt.emoji}</span>
                  <p className="font-semibold text-slate-800 text-sm">{opt.title}</p>
                  <p className="text-xs text-slate-500">{opt.desc}</p>
                </button>
              ))}
            </div>

            <Textarea
              label="Descripción de la situación"
              name="descripcion"
              value={form.descripcion}
              onChange={handleChange}
              placeholder="Describe dónde y cuándo, circunstancias, si fue visto por alguien…"
              rows={4}
              error={errs.descripcion}
            />

            <Button type="button" variant="primary" size="lg" className="w-full" onClick={nextStep}>
              Continuar →
            </Button>
          </Card>
        )}

        {/* ── Step 1: Mascota ── */}
        {step === 1 && (
          <Card className="space-y-5">
            <h2 className="font-bold text-slate-800 text-base">Datos de la mascota</h2>

            <div className="grid grid-cols-2 gap-4">
              <Input label="Nombre" name="nombreMascota" value={form.nombreMascota}
                onChange={handleChange} placeholder="Max" error={errs.nombreMascota} />
              <Select label="Especie" name="especie" value={form.especie}
                onChange={handleChange} options={ESPECIE_OPS} placeholder="" />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <Input label="Raza" name="raza" value={form.raza}
                onChange={handleChange} placeholder="Golden Retriever" error={errs.raza} />
              <Select label="Tamaño" name="tamaño" value={form.tamaño}
                onChange={handleChange} options={TAMAÑO_OPS} placeholder="" />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <Input label="Color" name="color" value={form.color}
                onChange={handleChange} placeholder="Marrón con blanco" />
              <Input label="Edad aprox. (años)" type="number" name="edad" value={form.edad}
                onChange={handleChange} placeholder="3" />
            </div>

            <Textarea label="Señas particulares" name="señasParticulares"
              value={form.señasParticulares} onChange={handleChange}
              placeholder="Collar rojo, cicatriz en pata, mancha en ojo…" rows={3} />

            <div className="flex gap-3">
              <Button type="button" variant="secondary" className="flex-1" onClick={prevStep}>← Atrás</Button>
              <Button type="button" variant="primary" className="flex-1" onClick={nextStep}>Continuar →</Button>
            </div>
          </Card>
        )}

        {/* ── Step 2: Ubicación ── */}
        {step === 2 && (
          <Card className="space-y-5">
            <h2 className="font-bold text-slate-800 text-base flex items-center gap-2">
              <FiMapPin size={18} className="text-primary-500" /> Ubicación del incidente
            </h2>

            <Input label="Ciudad" name="ciudad" value={form.ciudad}
              onChange={handleChange} placeholder="Santiago" error={errs.ciudad} />
            <Input label="Dirección (opcional)" name="direccion" value={form.direccion}
              onChange={handleChange} placeholder="Av. Providencia 1234" />

            <div className="grid grid-cols-2 gap-4">
              <Input label="Latitud" type="number" step="0.0001" name="latitud"
                value={form.latitud} onChange={handleChange} placeholder="-33.4489" />
              <Input label="Longitud" type="number" step="0.0001" name="longitud"
                value={form.longitud} onChange={handleChange} placeholder="-70.6693" />
            </div>

            <div className="bg-amber-50 border border-amber-200 rounded-xl p-3 flex gap-2">
              <FiInfo size={16} className="text-amber-600 flex-shrink-0 mt-0.5" />
              <p className="text-xs text-amber-700">
                Puedes obtener las coordenadas haciendo clic derecho en Google Maps → "¿Qué hay aquí?".
              </p>
            </div>

            <div className="flex gap-3">
              <Button type="button" variant="secondary" className="flex-1" onClick={prevStep}>← Atrás</Button>
              <Button type="submit" variant="accent" className="flex-1" loading={loading} size="lg">
                🐾 Publicar Reporte
              </Button>
            </div>
          </Card>
        )}
      </form>
    </div>
  )
}

export default CrearReportePage
