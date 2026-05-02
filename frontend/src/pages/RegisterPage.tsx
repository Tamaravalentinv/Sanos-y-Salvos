import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/context/authStore'
import Button from '@/components/Button'
import Input from '@/components/Input'
import Select from '@/components/Select'
import toast from 'react-hot-toast'

const RegisterPage: React.FC = () => {
  const navigate = useNavigate()
  const { register, error } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    confirmPassword: '',
    telefono: '',
    tipoUsuario: 'CIUDADANO',
    organizacion: '',
  })

  const [errors, setErrors] = useState<Record<string, string>>({})

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    // Clear error for this field
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const validate = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.nombre.trim()) newErrors.nombre = 'El nombre es requerido'
    if (!formData.apellido.trim()) newErrors.apellido = 'El apellido es requerido'
    if (!formData.email.includes('@'))
      newErrors.email = 'Email inválido'
    if (formData.password.length < 6)
      newErrors.password = 'La contraseña debe tener al menos 6 caracteres'
    if (formData.password !== formData.confirmPassword)
      newErrors.confirmPassword = 'Las contraseñas no coinciden'

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return

    setLoading(true)
    try {
      await register({
        nombre: formData.nombre,
        apellido: formData.apellido,
        email: formData.email,
        password: formData.password,
        telefono: formData.telefono,
        tipoUsuario: formData.tipoUsuario,
        organizacion: formData.organizacion || undefined,
      })
      toast.success('¡Registro exitoso! Bienvenido a Sanos y Salvos')
      navigate('/dashboard')
    } catch (err: any) {
      toast.error(error || 'Error al registrarse')
    } finally {
      setLoading(false)
    }
  }

  const userTypeOptions = [
    { value: 'CIUDADANO', label: 'Ciudadano' },
    { value: 'CLINICA', label: 'Clínica Veterinaria' },
    { value: 'REFUGIO', label: 'Refugio' },
    { value: 'MUNICIPALIDAD', label: 'Municipalidad' },
  ]

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 to-primary-800 flex items-center justify-center p-4 py-8">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white rounded-full mb-4">
            <span className="text-4xl">🐾</span>
          </div>
          <h1 className="text-3xl font-bold text-white mb-2">Sanos y Salvos</h1>
          <p className="text-primary-100">Únete a nuestra comunidad</p>
        </div>

        {/* Form Card */}
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Crear Cuenta</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <Input
                label="Nombre"
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                placeholder="Juan"
                error={errors.nombre}
              />
              <Input
                label="Apellido"
                name="apellido"
                value={formData.apellido}
                onChange={handleChange}
                placeholder="Pérez"
                error={errors.apellido}
              />
            </div>

            <Input
              label="Email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="tu@email.com"
              error={errors.email}
            />

            <Input
              label="Teléfono (Opcional)"
              type="tel"
              name="telefono"
              value={formData.telefono}
              onChange={handleChange}
              placeholder="+34 600 000 000"
            />

            <Select
              label="Tipo de Usuario"
              name="tipoUsuario"
              value={formData.tipoUsuario}
              onChange={handleChange}
              options={userTypeOptions}
            />

            {formData.tipoUsuario !== 'CIUDADANO' && (
              <Input
                label="Nombre de Organización"
                name="organizacion"
                value={formData.organizacion}
                onChange={handleChange}
                placeholder="Mi Clínica Veterinaria"
              />
            )}

            <Input
              label="Contraseña"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              error={errors.password}
            />

            <Input
              label="Confirmar Contraseña"
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="••••••••"
              error={errors.confirmPassword}
            />

            <Button
              type="submit"
              variant="primary"
              size="lg"
              loading={loading}
              className="w-full mt-6"
            >
              Crear Cuenta
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-gray-600">
              ¿Ya tienes cuenta?{' '}
              <a
                href="/login"
                className="text-primary-600 font-semibold hover:text-primary-700"
              >
                Inicia sesión aquí
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RegisterPage
