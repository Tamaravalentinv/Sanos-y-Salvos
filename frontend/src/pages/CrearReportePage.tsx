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
import { useNotificacionStore } from '@/context/notificacionStore'
import { ApiError, Notificacion } from '@/types'
import toast from 'react-hot-toast'
import { AxiosError } from 'axios'

function calcularDistanciaKm(lat1: number, lng1: number, lat2: number, lng2: number): number {
  const R = 6371
  const dLat = (lat2 - lat1) * Math.PI / 180
  const dLng = (lng2 - lng1) * Math.PI / 180
  const a = Math.sin(dLat / 2) ** 2 + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLng / 2) ** 2
  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
}

// ── Matching helpers ──────────────────────────────────────────────────────────

function normText(s: string): string {
  return s.toLowerCase().trim()
    .replace(/[áàä]/g, 'a').replace(/[éèë]/g, 'e')
    .replace(/[íìï]/g, 'i').replace(/[óòö]/g, 'o').replace(/[úùü]/g, 'u')
}

function puntajeRaza(r1: string, r2: string): { pts: number; detalle: string } {
  const n1 = normText(r1), n2 = normText(r2)
  const vacios = ['desconocida', 'desconocido', 'mestizo', 'mestiza', 'no se', '']
  if (vacios.includes(n1) || vacios.includes(n2)) return { pts: 0, detalle: '' }
  if (n1 === n2) return { pts: 35, detalle: `misma raza (${r1})` }
  if (n1.includes(n2) || n2.includes(n1)) return { pts: 25, detalle: `raza muy similar (${r1} / ${r2})` }
  const w1 = n1.split(/\s+/).filter(w => w.length > 3)
  const w2 = n2.split(/\s+/).filter(w => w.length > 3)
  if (w1.length && w2.length && w1.some(a => w2.some(b => a.includes(b) || b.includes(a)))) {
    return { pts: 15, detalle: `raza relacionada (${r1} / ${r2})` }
  }
  return { pts: 0, detalle: '' }
}

function puntajeColor(c1: string, c2: string): { pts: number; detalle: string } {
  if (!c1 || !c2) return { pts: 0, detalle: '' }
  const t1 = normText(c1).split(/[\s,/]+/).filter(t => t.length > 2)
  const t2 = normText(c2).split(/[\s,/]+/).filter(t => t.length > 2)
  if (!t1.length || !t2.length) return { pts: 0, detalle: '' }
  let matches = 0
  for (const a of t1) {
    if (t2.some(b => a.includes(b) || b.includes(a))) matches++
  }
  if (!matches) return { pts: 0, detalle: '' }
  const pts = Math.round((matches / Math.max(t1.length, t2.length)) * 25)
  return { pts, detalle: pts >= 20 ? `mismo color (${c1})` : `color similar (${c1} / ${c2})` }
}

const TAMAÑOS = ['PEQUEÑO', 'MEDIANO', 'GRANDE']
function puntajeTamaño(t1: string, t2: string): { pts: number; detalle: string } {
  if (t1 === t2) return { pts: 15, detalle: `mismo tamaño (${t1.toLowerCase()})` }
  const i1 = TAMAÑOS.indexOf(t1), i2 = TAMAÑOS.indexOf(t2)
  if (i1 !== -1 && i2 !== -1 && Math.abs(i1 - i2) === 1) return { pts: 5, detalle: 'tamaño similar' }
  return { pts: 0, detalle: '' }
}

function puntajeProximidad(lat1: number, lng1: number, lat2: number, lng2: number): { pts: number; detalle: string; dist: number } {
  if (!lat1 || !lng1 || !lat2 || !lng2) return { pts: 0, detalle: '', dist: -1 }
  const dist = calcularDistanciaKm(lat1, lng1, lat2, lng2)
  if (dist < 1)  return { pts: 20, detalle: `muy cerca (${dist.toFixed(1)} km)`, dist }
  if (dist < 3)  return { pts: 17, detalle: `zona cercana (${dist.toFixed(1)} km)`, dist }
  if (dist < 7)  return { pts: 13, detalle: `zona próxima (${dist.toFixed(1)} km)`, dist }
  if (dist < 15) return { pts: 8,  detalle: `misma área (${dist.toFixed(1)} km)`, dist }
  if (dist < 25) return { pts: 4,  detalle: `zona amplia (${dist.toFixed(1)} km)`, dist }
  return { pts: 0, detalle: '', dist }
}

function puntajeFecha(f1: string, f2: string): number {
  if (!f1 || !f2) return 1
  const dias = Math.abs(new Date(f1).getTime() - new Date(f2).getTime()) / 86400000
  if (dias <= 3)  return 5
  if (dias <= 7)  return 4
  if (dias <= 14) return 3
  if (dias <= 30) return 2
  return 1
}

// ─────────────────────────────────────────────────────────────────────────────

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
  const { addNotificacion } = useNotificacionStore()
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
        fotoBase64: photoPreview ?? undefined,
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

      // Buscar coincidencias reales con reportes del tipo opuesto
      try {
        const tipoOpuesto = form.tipo === 'PERDIDO' ? 'ENCONTRADO' : 'PERDIDO'
        const { content: todos } = await reporteService.getAllReportes({ size: 200 })
        const opuestos = todos.filter(r => r.tipo === tipoOpuesto && r.estado === 'ACTIVO')

        // La especie del formulario se almacena en backend como PERRO, GATO u OTRO
        const especieAlmacenada =
          especieFinal === 'PERRO' ? 'PERRO' : especieFinal === 'GATO' ? 'GATO' : 'OTRO'
        const lat = parseFloat(form.latitud) || 0
        const lng = parseFloat(form.longitud) || 0
        const ahora = new Date().toISOString()

        const matches = opuestos
          // Prerequisito estricto: misma especie exacta
          .filter(r => r.mascota?.especie === especieAlmacenada)
          .map(r => {
            const raza  = puntajeRaza(form.raza, r.mascota?.raza ?? '')
            const color = puntajeColor(form.color, r.mascota?.color ?? '')
            const tam   = puntajeTamaño(form.tamaño, r.mascota?.tamaño ?? '')
            const prox  = puntajeProximidad(lat, lng, r.ubicacion?.latitud ?? 0, r.ubicacion?.longitud ?? 0)
            const fecha = puntajeFecha(ahora, r.fechaReporte)

            const score = raza.pts + color.pts + tam.pts + prox.pts + fecha
            const factores = [raza.detalle, color.detalle, tam.detalle, prox.detalle].filter(Boolean)

            return { reporte: r, score, factores, dist: prox.dist }
          })
          // Umbral: 45/100 puntos Y al menos un factor nombrado — especie sola no alcanza
          .filter(m => m.score >= 45 && m.factores.length >= 1)
          .sort((a, b) => b.score - a.score)
          .slice(0, 3)

        for (const match of matches) {
          const distTexto = match.dist > 0 ? ` a ${match.dist.toFixed(1)} km` : ''
          const ciudadTexto = match.reporte.ubicacion?.ciudad ? `, ${match.reporte.ubicacion.ciudad}` : ''
          const notif: Notificacion = {
            id: `match_${Date.now()}_${match.reporte.id}`,
            titulo: '🎯 Posible coincidencia detectada',
            mensaje: `"${match.reporte.mascota?.nombre ?? 'Animal sin nombre'}" fue reportado como ${match.reporte.tipo === 'ENCONTRADO' ? 'encontrado' : 'perdido'}${distTexto}${ciudadTexto}. Coincide en: ${match.factores.join(', ')}.`,
            tipo: 'COINCIDENCIA',
            estado: 'NO_LEIDA',
            usuarioId: user?.id ?? '',
            relatedReporteId: match.reporte.id,
            fechaCreacion: new Date().toISOString(),
            canalEnvio: 'INTERNO',
          }
          addNotificacion(notif)
        }
        if (matches.length > 0) {
          toast.success(
            `¡${matches.length} coincidencia${matches.length > 1 ? 's' : ''} real${matches.length > 1 ? 'es' : ''} detectada${matches.length > 1 ? 's' : ''}! Revisa tus notificaciones 🔔`,
            { duration: 6000 }
          )
        }
      } catch {
        // matching failure is non-fatal
      }

      navigate('/mapa')
    } catch (err: unknown) {
      const error = err as AxiosError<ApiError>
      toast.error(error.response?.data?.message || 'Error al crear el reporte')
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
