import React, { useState, useRef } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { FiArrowLeft, FiMapPin, FiCheckCircle, FiCamera, FiX } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Input from '@/components/Input'
import Select from '@/components/Select'
import Textarea from '@/components/Textarea'
import MapPicker from '@/components/MapPicker'
import { reporteService } from '@/services/reporte.service'
import { useAuthStore } from '@/context/authStore'
import toast from 'react-hot-toast'

const ESPECIE_OPS = [
  { value: 'PERRO',   label: '🐕 Perro' },
  { value: 'GATO',    label: '🐈 Gato' },
  { value: 'CONEJO',  label: '🐇 Conejo' },
  { value: 'PAJARO',  label: '🐦 Pájaro' },
  { value: 'HAMSTER', label: '🐹 Hámster / Roedor' },
  { value: 'REPTIL',  label: '🦎 Reptil' },
  { value: 'OTRO',    label: '🐾 Otro' },
]

const TAMAÑO_OPS = [
  { value: 'PEQUEÑO', label: 'Pequeño (< 10 kg)' },
  { value: 'MEDIANO', label: 'Mediano (10–25 kg)' },
  { value: 'GRANDE',  label: 'Grande (> 25 kg)' },
]

const STEP_LABELS = ['Tipo de reporte', 'Mascota', 'Ubicación']

const CrearReportePage: React.FC = () => {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { user } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [step, setStep] = useState(0)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const [form, setForm] = useState({
    tipo: 'PERDIDO',
    descripcion: '',
    nombreMascota: '',
    especie: 'PERRO',
    especieCustom: '',
    raza: '',
    color: '',
    tamaño: 'MEDIANO',
    edad: '',
    señasParticulares: '',
    ciudad: '',
    direccion: '',
    latitud: searchParams.get('lat') || '',
    longitud: searchParams.get('lng') || '',
  })
  const [photoPreview, setPhotoPreview] = useState<string | null>(null)
  const [errs, setErrs] = useState<Record<string, string>>({})

  const isPerdido = form.tipo === 'PERDIDO'

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
    if (errs[name]) setErrs(prev => ({ ...prev, [name]: '' }))
  }

  const handlePhotoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return
    const reader = new FileReader()
    reader.onload = ev => setPhotoPreview(ev.target?.result as string)
    reader.readAsDataURL(file)
  }

  const removePhoto = () => {
    setPhotoPreview(null)
    if (fileInputRef.current) fileInputRef.current.value = ''
  }

  const validateStep = (s: number) => {
    const e: Record<string, string> = {}
    if (s === 0 && !form.descripcion.trim()) e.descripcion = 'Requerida'
    if (s === 1) {
      if (isPerdido && !form.nombreMascota.trim()) e.nombreMascota = 'Requerido'
      if (isPerdido && !form.raza.trim()) e.raza = 'Requerida'
      if (form.especie === 'OTRO' && !form.especieCustom.trim()) e.especieCustom = 'Indica el tipo de animal'
    }
    if (s === 2 && !form.ciudad.trim()) e.ciudad = 'Selecciona una ubicación en el mapa o escribe la ciudad'
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
      const especieFinal = form.especie === 'OTRO' ? (form.especieCustom.trim() || 'OTRO') : form.especie
      await reporteService.createReporte({
        tipo: form.tipo,
        descripcion: form.descripcion,
        mascota: {
          nombre: form.nombreMascota.trim() || 'Sin nombre',
          especie: especieFinal,
          raza: form.raza.trim() || 'Desconocida',
          color: form.color,
          tamaño: form.tamaño,
          señas_particulares: form.señasParticulares,
        },
        ubicacion: {
          latitud: parseFloat(form.latitud) || 0,
          longitud: parseFloat(form.longitud) || 0,
          direccion: form.direccion,
          ciudad: form.ciudad,
          pais: 'Chile',
        },
        usuarioId: user?.id ?? '1',
      })
      toast.success('¡Reporte creado exitosamente! 🐾')
      navigate('/reportes')
    } catch (err: any) {
      toast.error(err?.response?.data?.message || 'Error al crear el reporte')
    } finally {
      setLoading(false)
    }
  }

  const lat = form.latitud ? parseFloat(form.latitud) : undefined
  const lng = form.longitud ? parseFloat(form.longitud) : undefined
  const hasLocation = !!form.latitud && !!form.longitud

  return (
    <div className="max-w-2xl mx-auto space-y-6 fade-in">
      {/* Header */}
      <div className="flex items-center gap-3">
        <Button variant="ghost" size="sm" onClick={() => navigate('/reportes')}>
          <FiArrowLeft size={16} /> Volver
        </Button>
        <div>
          <h1 className="text-xl font-bold text-slate-900">Crear Reporte</h1>
          <p className="text-slate-500 text-sm">
            {STEP_LABELS[step]} · Paso {step + 1} de 3
          </p>
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
                {
                  value: 'PERDIDO',
                  emoji: '😟',
                  title: 'Perdí a mi mascota',
                  desc: 'Quiero encontrar a mi mascota',
                },
                {
                  value: 'ENCONTRADO',
                  emoji: '🤗',
                  title: 'Encontré una mascota',
                  desc: 'Encontré un animal extraviado',
                },
              ].map(opt => (
                <button
                  key={opt.value}
                  type="button"
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
              label={isPerdido ? 'Describe la situación' : 'Cuéntanos dónde y cómo lo encontraste'}
              name="descripcion"
              value={form.descripcion}
              onChange={handleChange}
              placeholder={
                isPerdido
                  ? 'Ej: Se escapó del jardín ayer por la tarde cerca del parque…'
                  : 'Ej: Lo encontré deambulando solo en la calle, parece asustado y hambriento…'
              }
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
            <div>
              <h2 className="font-bold text-slate-800 text-base">
                {isPerdido ? 'Datos de tu mascota' : 'Describe al animal encontrado'}
              </h2>
              {!isPerdido && (
                <p className="text-xs text-slate-400 mt-0.5">
                  No pasa nada si no sabes todos los datos. Llena lo que puedas — la foto ayuda mucho.
                </p>
              )}
            </div>

            {/* Photo upload */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">
                Foto del animal{' '}
                {!isPerdido && (
                  <span className="text-accent-600 font-normal">(muy recomendada — ayuda a identificarlo)</span>
                )}
              </label>
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*"
                className="hidden"
                onChange={handlePhotoChange}
              />
              {photoPreview ? (
                <div className="relative w-full h-48 rounded-xl overflow-hidden border border-slate-200">
                  <img src={photoPreview} alt="Vista previa" className="w-full h-full object-cover" />
                  <button
                    type="button"
                    onClick={removePhoto}
                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1.5 hover:bg-red-600 transition shadow"
                  >
                    <FiX size={13} />
                  </button>
                </div>
              ) : (
                <button
                  type="button"
                  onClick={() => fileInputRef.current?.click()}
                  className="w-full h-32 border-2 border-dashed border-slate-300 rounded-xl flex flex-col items-center justify-center gap-2 text-slate-400 hover:border-primary-400 hover:text-primary-500 transition"
                >
                  <FiCamera size={26} />
                  <span className="text-sm font-medium">Toca para agregar una foto</span>
                  <span className="text-xs">JPG, PNG, WEBP</span>
                </button>
              )}
            </div>

            {/* Nombre */}
            <Input
              label={isPerdido ? 'Nombre *' : 'Nombre o apodo temporal (opcional)'}
              name="nombreMascota"
              value={form.nombreMascota}
              onChange={handleChange}
              placeholder={isPerdido ? 'Max' : 'Si le pusiste algún apodo…'}
              error={errs.nombreMascota}
            />

            <div className="grid grid-cols-2 gap-4">
              <Select
                label="Especie"
                name="especie"
                value={form.especie}
                onChange={handleChange}
                options={ESPECIE_OPS}
                placeholder=""
              />
              <Select
                label="Tamaño"
                name="tamaño"
                value={form.tamaño}
                onChange={handleChange}
                options={TAMAÑO_OPS}
                placeholder=""
              />
            </div>

            {/* Custom species when OTRO */}
            {form.especie === 'OTRO' && (
              <Input
                label="¿Qué tipo de animal? *"
                name="especieCustom"
                value={form.especieCustom}
                onChange={handleChange}
                placeholder="Ej: Tortuga, Cabra, Pez…"
                error={errs.especieCustom}
              />
            )}

            <div className="grid grid-cols-2 gap-4">
              <Input
                label={isPerdido ? 'Raza *' : 'Raza (si la sabes)'}
                name="raza"
                value={form.raza}
                onChange={handleChange}
                placeholder={isPerdido ? 'Golden Retriever' : 'Mestizo, no sé…'}
                error={errs.raza}
              />
              <Input
                label="Color principal"
                name="color"
                value={form.color}
                onChange={handleChange}
                placeholder="Marrón con blanco"
              />
            </div>

            {isPerdido && (
              <Input
                label="Edad aprox. (años)"
                type="number"
                name="edad"
                value={form.edad}
                onChange={handleChange}
                placeholder="3"
              />
            )}

            <Textarea
              label={isPerdido ? 'Señas particulares' : 'Señas particulares (collar, chip, heridas, estado…)'}
              name="señasParticulares"
              value={form.señasParticulares}
              onChange={handleChange}
              placeholder={
                isPerdido
                  ? 'Collar rojo, cicatriz en pata, mancha en ojo…'
                  : 'Tenía collar azul sin placa, parecía asustado, cojeaba de la pata derecha…'
              }
              rows={3}
            />

            <div className="flex gap-3">
              <Button type="button" variant="secondary" className="flex-1" onClick={prevStep}>
                ← Atrás
              </Button>
              <Button type="button" variant="primary" className="flex-1" onClick={nextStep}>
                Continuar →
              </Button>
            </div>
          </Card>
        )}

        {/* ── Step 2: Ubicación ── */}
        {step === 2 && (
          <Card className="space-y-5">
            <h2 className="font-bold text-slate-800 text-base flex items-center gap-2">
              <FiMapPin size={18} className="text-primary-500" />
              {isPerdido ? '¿Dónde se perdió?' : '¿Dónde lo encontraste?'}
            </h2>

            <div className="space-y-2">
              <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
                Haz clic en el mapa para marcar el lugar exacto
              </p>
              <MapPicker
                lat={lat}
                lng={lng}
                height={340}
                onLocationSelect={(selLat, selLng, address, city) => {
                  setForm(prev => ({
                    ...prev,
                    latitud: selLat.toFixed(6),
                    longitud: selLng.toFixed(6),
                    direccion: address || prev.direccion,
                    ciudad: city || prev.ciudad,
                  }))
                  setErrs(prev => ({ ...prev, ciudad: '' }))
                }}
              />
            </div>

            {hasLocation && (
              <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-3 flex gap-2">
                <FiCheckCircle size={16} className="text-emerald-600 flex-shrink-0 mt-0.5" />
                <div className="text-xs text-emerald-700 space-y-0.5">
                  <p className="font-semibold">Ubicación seleccionada</p>
                  {form.ciudad && <p>📍 {form.ciudad}</p>}
                  {form.direccion && <p>{form.direccion}</p>}
                  <p className="text-emerald-600 font-mono">
                    {parseFloat(form.latitud).toFixed(5)}, {parseFloat(form.longitud).toFixed(5)}
                  </p>
                </div>
              </div>
            )}

            <div className="space-y-3 pt-1 border-t border-slate-100">
              <p className="text-xs text-slate-400">O completa manualmente si prefieres:</p>
              <Input
                label="Ciudad"
                name="ciudad"
                value={form.ciudad}
                onChange={handleChange}
                placeholder="Santiago"
                error={errs.ciudad}
              />
              <Input
                label="Dirección (opcional)"
                name="direccion"
                value={form.direccion}
                onChange={handleChange}
                placeholder="Av. Providencia 1234"
              />
            </div>

            <div className="flex gap-3">
              <Button type="button" variant="secondary" className="flex-1" onClick={prevStep}>
                ← Atrás
              </Button>
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
