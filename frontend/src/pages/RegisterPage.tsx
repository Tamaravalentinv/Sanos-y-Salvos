import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { FiUser, FiMail, FiLock, FiPhone, FiArrowRight, FiArrowLeft } from 'react-icons/fi'
import { useAuthStore } from '@/context/authStore'
import Input from '@/components/Input'
import Select from '@/components/Select'
import Button from '@/components/Button'
import toast from 'react-hot-toast'
import { AxiosError } from 'axios'
import { ApiError } from '@/types'

const USER_TYPES = [
  { value: 'CIUDADANO',    label: '🏠 Ciudadano',               desc: 'Persona que perdió o encontró una mascota' },
  { value: 'CLINICA',      label: '🏥 Clínica Veterinaria',     desc: 'Centro de atención veterinaria' },
  { value: 'REFUGIO',      label: '🏠 Refugio de animales',     desc: 'Organización de rescate y adopción' },
  { value: 'MUNICIPALIDAD',label: '🏛️ Municipalidad',          desc: 'Organismo gubernamental local' },
]

const RegisterPage: React.FC = () => {
  const navigate = useNavigate()
  const { register } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({
    nombre: '', apellido: '', email: '',
    password: '', confirmPassword: '',
    telefono: '', tipoUsuario: 'CIUDADANO', organizacion: '',
  })
  const [errs, setErrs] = useState<Record<string, string>>({})

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
    if (errs[name]) setErrs(prev => ({ ...prev, [name]: '' }))
  }

  const validate = () => {
    const e: Record<string, string> = {}
    if (!form.nombre.trim())         e.nombre = 'Requerido'
    if (!form.apellido.trim())       e.apellido = 'Requerido'
    if (!form.email.includes('@'))   e.email = 'Email inválido'
    if (form.password.length < 6)    e.password = 'Mínimo 6 caracteres'
    if (form.password !== form.confirmPassword) e.confirmPassword = 'Las contraseñas no coinciden'
    setErrs(e)
    return Object.keys(e).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return
    setLoading(true)
    try {
      await register({
        nombre: form.nombre, apellido: form.apellido,
        email: form.email, password: form.password,
        telefono: form.telefono || undefined,
        tipoUsuario: form.tipoUsuario,
        organizacion: form.organizacion || undefined,
      })
      toast.success('¡Cuenta creada exitosamente!')
      navigate('/dashboard')
    } catch (err: unknown) {
      const error = err as AxiosError<ApiError>
      toast.error(error.response?.data?.message || 'Error al registrarse')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 flex items-center justify-center p-4 py-10">
      <div className="w-full max-w-lg fade-in">
        {/* Back to login */}
        <Link to="/login" className="inline-flex items-center gap-2 text-primary-200 hover:text-white text-sm mb-6 transition-colors">
          <FiArrowLeft size={16} /> Volver al login
        </Link>

        <div className="bg-white rounded-3xl shadow-2xl overflow-hidden">
          {/* Top gradient bar */}
          <div className="h-2 bg-gradient-to-r from-primary-400 via-accent-400 to-accent-500" />

          <div className="p-8">
            {/* Header */}
            <div className="flex items-center gap-3 mb-7">
              <div className="w-12 h-12 bg-primary-50 rounded-2xl flex items-center justify-center text-2xl">🐾</div>
              <div>
                <h2 className="text-xl font-bold text-slate-900">Crear cuenta</h2>
                <p className="text-slate-500 text-sm">Únete a la comunidad Sanos y Salvos</p>
              </div>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <Input label="Nombre" name="nombre" value={form.nombre}
                  onChange={handleChange} placeholder="Juan" error={errs.nombre}
                  icon={<FiUser size={14} />} required />
                <Input label="Apellido" name="apellido" value={form.apellido}
                  onChange={handleChange} placeholder="Pérez" error={errs.apellido} required />
              </div>

              <Input label="Email" type="email" name="email" value={form.email}
                onChange={handleChange} placeholder="tu@email.com" error={errs.email}
                icon={<FiMail size={14} />} required />

              <Input label="Teléfono (opcional)" type="tel" name="telefono" value={form.telefono}
                onChange={handleChange} placeholder="+56 9 1234 5678"
                icon={<FiPhone size={14} />} />

              <Select
                label="Tipo de cuenta"
                name="tipoUsuario"
                value={form.tipoUsuario}
                onChange={handleChange}
                options={USER_TYPES.map(t => ({ value: t.value, label: t.label }))}
                placeholder=""
              />

              {/* Show selected type description */}
              {form.tipoUsuario && (
                <p className="text-xs text-slate-500 bg-primary-50 rounded-xl px-3 py-2">
                  {USER_TYPES.find(t => t.value === form.tipoUsuario)?.desc}
                </p>
              )}

              {form.tipoUsuario !== 'CIUDADANO' && (
                <Input label="Nombre de organización" name="organizacion" value={form.organizacion}
                  onChange={handleChange} placeholder="Refugio Los Peludos" />
              )}

              <div className="grid grid-cols-2 gap-4">
                <Input label="Contraseña" type="password" name="password" value={form.password}
                  onChange={handleChange} placeholder="••••••••" error={errs.password}
                  icon={<FiLock size={14} />} required />
                <Input label="Confirmar" type="password" name="confirmPassword"
                  value={form.confirmPassword} onChange={handleChange}
                  placeholder="••••••••" error={errs.confirmPassword}
                  icon={<FiLock size={14} />} required />
              </div>

              <Button type="submit" variant="primary" size="lg" loading={loading} className="w-full mt-2">
                Crear mi cuenta <FiArrowRight size={16} />
              </Button>
            </form>

            <p className="text-center text-sm text-slate-500 mt-6">
              ¿Ya tienes cuenta?{' '}
              <Link to="/login" className="text-primary-600 font-semibold hover:text-primary-700 transition-colors">
                Inicia sesión
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RegisterPage
