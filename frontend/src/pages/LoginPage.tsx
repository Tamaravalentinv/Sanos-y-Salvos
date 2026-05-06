import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { FiMail, FiLock, FiArrowRight } from 'react-icons/fi'
import { useAuthStore } from '@/context/authStore'
import Input from '@/components/Input'
import Button from '@/components/Button'
import toast from 'react-hot-toast'

const PawSVG = () => (
  <svg viewBox="0 0 200 200" fill="none" className="w-full h-full opacity-10" aria-hidden>
    <ellipse cx="60" cy="50" rx="22" ry="28" fill="white" />
    <ellipse cx="100" cy="35" rx="18" ry="23" fill="white" />
    <ellipse cx="140" cy="50" rx="22" ry="28" fill="white" />
    <ellipse cx="30" cy="90" rx="16" ry="20" fill="white" />
    <ellipse cx="170" cy="90" rx="16" ry="20" fill="white" />
    <ellipse cx="100" cy="140" rx="50" ry="44" fill="white" />
  </svg>
)

const stats = [
  { value: '2,400+', label: 'Mascotas encontradas' },
  { value: '15K+',   label: 'Usuarios activos' },
  { value: '98%',    label: 'Tasa de respuesta' },
]

const LoginPage: React.FC = () => {
  const navigate = useNavigate()
  const { login } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({ email: '', password: '' })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      await login(form.email, form.password)
      toast.success('¡Bienvenido de vuelta!')
      navigate('/dashboard')
    } catch {
      toast.error('Email o contraseña incorrectos')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex">
      {/* ── Left panel ─────────────────────────────────────────── */}
      <div className="hidden lg:flex lg:w-1/2 relative flex-col justify-between
                      bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 p-12 overflow-hidden">
        {/* Background paw pattern */}
        <div className="absolute inset-0 grid grid-cols-3 gap-16 p-16 pointer-events-none">
          {Array.from({ length: 9 }).map((_, i) => (
            <div key={i} className="w-24 h-24 opacity-5">
              <PawSVG />
            </div>
          ))}
        </div>

        {/* Logo */}
        <div className="relative z-10 flex items-center gap-3">
          <div className="w-10 h-10 bg-white/20 backdrop-blur rounded-xl flex items-center justify-center text-2xl">
            🐾
          </div>
          <span className="text-white font-bold text-xl">Sanos y Salvos</span>
        </div>

        {/* Hero text + big paw */}
        <div className="relative z-10 space-y-8">
          <div className="space-y-4">
            <div className="text-8xl animate-bounce" style={{ animationDuration: '3s' }}>🐾</div>
            <h1 className="text-4xl font-bold text-white leading-tight">
              Encuentra a tu<br />
              <span className="text-accent-300">mascota perdida</span><br />
              con la comunidad.
            </h1>
            <p className="text-primary-200 text-lg leading-relaxed max-w-sm">
              Conectamos familias con sus compañeros. Reporta, busca y recibe alertas
              en tiempo real cerca de ti.
            </p>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-3 gap-4">
            {stats.map(s => (
              <div key={s.value} className="bg-white/10 backdrop-blur rounded-2xl p-4 text-center border border-white/10">
                <p className="text-2xl font-bold text-white">{s.value}</p>
                <p className="text-primary-200 text-xs mt-1">{s.label}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Bottom tagline */}
        <p className="relative z-10 text-primary-300 text-sm">
          Plataforma de recuperación de mascotas • Gratuita para siempre
        </p>
      </div>

      {/* ── Right panel ────────────────────────────────────────── */}
      <div className="flex-1 flex items-center justify-center p-6 bg-slate-50">
        <div className="w-full max-w-md fade-in">
          {/* Mobile logo */}
          <div className="lg:hidden flex items-center gap-2 mb-8">
            <span className="text-3xl">🐾</span>
            <span className="font-bold text-xl text-slate-900">Sanos y Salvos</span>
          </div>

          <div className="bg-white rounded-3xl shadow-[0_8px_30px_rgb(0,0,0,0.08)] p-8 border border-slate-100">
            <div className="mb-8">
              <h2 className="text-2xl font-bold text-slate-900">Iniciar sesión</h2>
              <p className="text-slate-500 text-sm mt-1">Ingresa tus credenciales para continuar</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              <Input
                label="Email"
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="tu@email.com"
                icon={<FiMail size={16} />}
                required
                autoComplete="email"
              />
              <Input
                label="Contraseña"
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="••••••••"
                icon={<FiLock size={16} />}
                required
                autoComplete="current-password"
              />

              <Button
                type="submit"
                variant="primary"
                size="lg"
                loading={loading}
                className="w-full mt-2"
              >
                Ingresar <FiArrowRight size={16} />
              </Button>
            </form>

            {/* Divider */}
            <div className="my-6 flex items-center gap-3">
              <div className="flex-1 h-px bg-slate-100" />
              <span className="text-xs text-slate-400">o</span>
              <div className="flex-1 h-px bg-slate-100" />
            </div>

            <p className="text-center text-sm text-slate-500">
              ¿No tienes cuenta?{' '}
              <Link to="/register" className="text-primary-600 font-semibold hover:text-primary-700 transition-colors">
                Regístrate gratis
              </Link>
            </p>
          </div>

          {/* Demo hint */}
          <div className="mt-4 p-4 bg-accent-50 border border-accent-200 rounded-2xl">
            <p className="text-xs font-semibold text-accent-700 mb-1">💡 Credenciales de prueba</p>
            <p className="text-xs text-accent-600">Email: <strong>demo@sanosysalvos.cl</strong> · Pass: <strong>demo123456</strong></p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default LoginPage
