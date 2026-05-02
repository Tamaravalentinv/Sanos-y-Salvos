import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/context/authStore'
import Button from '@/components/Button'
import Input from '@/components/Input'
import toast from 'react-hot-toast'

const LoginPage: React.FC = () => {
  const navigate = useNavigate()
  const { login, error } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      await login(formData.email, formData.password)
      toast.success('¡Bienvenido!')
      navigate('/dashboard')
    } catch (err: any) {
      toast.error(error || 'Error al iniciar sesión')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 to-primary-800 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white rounded-full mb-4">
            <span className="text-4xl">🐾</span>
          </div>
          <h1 className="text-3xl font-bold text-white mb-2">Sanos y Salvos</h1>
          <p className="text-primary-100">Recupera tu mascota con ayuda de la comunidad</p>
        </div>

        {/* Form Card */}
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Iniciar Sesión</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="tu@email.com"
              required
            />

            <Input
              label="Contraseña"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              required
            />

            <Button
              type="submit"
              variant="primary"
              size="lg"
              loading={loading}
              className="w-full mt-6"
            >
              Iniciar Sesión
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-gray-600">
              ¿No tienes cuenta?{' '}
              <a
                href="/register"
                className="text-primary-600 font-semibold hover:text-primary-700"
              >
                Regístrate aquí
              </a>
            </p>
          </div>
        </div>

        {/* Demo credentials */}
        <div className="mt-6 bg-white bg-opacity-10 rounded-lg p-4 text-white text-sm">
          <p className="font-semibold mb-2">Credenciales de prueba:</p>
          <p>Email: demo@example.com</p>
          <p>Contraseña: demo123456</p>
        </div>
      </div>
    </div>
  )
}

export default LoginPage
