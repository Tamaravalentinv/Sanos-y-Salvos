import React, { useState, useEffect } from 'react'
import { FiEdit2, FiSave, FiX } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import Input from '@/components/Input'
import { useAuthStore } from '@/context/authStore'
import { authService } from '@/services/auth.service'
import { User } from '@/types'
import { getInitials } from '@/utils/helpers'
import toast from 'react-hot-toast'

const PerfilPage: React.FC = () => {
  const { user, setUser } = useAuthStore()
  const [editing, setEditing] = useState(false)
  const [loading, setLoading] = useState(false)
  const [changing, setChanging] = useState(false)
  const [formData, setFormData] = useState<User>(
    user || {
      id: '',
      nombre: '',
      apellido: '',
      email: '',
      telefono: '',
      tipoUsuario: 'CIUDADANO',
    }
  )
  const [passwordData, setPasswordData] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  })
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    if (user) {
      setFormData(user)
    }
  }, [user])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setPasswordData((prev) => ({ ...prev, [name]: value }))
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const validateProfile = () => {
    const newErrors: Record<string, string> = {}
    if (!formData.nombre.trim()) newErrors.nombre = 'El nombre es requerido'
    if (!formData.apellido.trim()) newErrors.apellido = 'El apellido es requerido'
    if (!formData.email.includes('@')) newErrors.email = 'Email inválido'
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const validatePassword = () => {
    const newErrors: Record<string, string> = {}
    if (!passwordData.oldPassword)
      newErrors.oldPassword = 'Contraseña actual requerida'
    if (passwordData.newPassword.length < 6)
      newErrors.newPassword = 'Contraseña debe tener al menos 6 caracteres'
    if (passwordData.newPassword !== passwordData.confirmPassword)
      newErrors.confirmPassword = 'Las contraseñas no coinciden'
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleUpdateProfile = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validateProfile() || !user) return

    setLoading(true)
    try {
      const updated = await authService.updateProfile(user.id, formData)
      setUser(updated)
      setEditing(false)
      toast.success('Perfil actualizado exitosamente')
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Error al actualizar perfil')
    } finally {
      setLoading(false)
    }
  }

  const handleChangePassword = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validatePassword()) return

    setChanging(true)
    try {
      await authService.changePassword(
        passwordData.oldPassword,
        passwordData.newPassword
      )
      setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' })
      toast.success('Contraseña actualizada exitosamente')
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Error al cambiar contraseña')
    } finally {
      setChanging(false)
    }
  }

  if (!user) {
    return (
      <div className="flex items-center justify-center h-96">
        <p className="text-gray-600">Cargando perfil...</p>
      </div>
    )
  }

  return (
    <div className="space-y-6 max-w-2xl">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Mi Perfil</h1>
        <p className="text-gray-600 mt-1">Administra tu información personal</p>
      </div>

      {/* Profile Card */}
      <Card>
        <div className="space-y-6">
          {/* Avatar and Basic Info */}
          <div className="flex items-center gap-6">
            <div className="w-24 h-24 bg-primary-600 text-white rounded-full flex items-center justify-center font-bold text-3xl">
              {getInitials(user.nombre, user.apellido)}
            </div>
            <div>
              <h2 className="text-2xl font-bold text-gray-900">
                {user.nombre} {user.apellido}
              </h2>
              <p className="text-gray-600">{user.email}</p>
              <p className="text-sm text-gray-500 mt-1">{user.tipoUsuario}</p>
            </div>
            {!editing && (
              <Button
                variant="secondary"
                onClick={() => setEditing(true)}
                className="ml-auto"
              >
                <FiEdit2 size={18} />
                Editar
              </Button>
            )}
          </div>

          {/* Edit Form */}
          {editing && (
            <form onSubmit={handleUpdateProfile} className="space-y-4 border-t pt-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Input
                  label="Nombre"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  error={errors.nombre}
                />
                <Input
                  label="Apellido"
                  name="apellido"
                  value={formData.apellido}
                  onChange={handleChange}
                  error={errors.apellido}
                />
              </div>

              <Input
                label="Email"
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                error={errors.email}
              />

              <Input
                label="Teléfono"
                type="tel"
                name="telefono"
                value={formData.telefono || ''}
                onChange={handleChange}
              />

              <div className="flex gap-2 pt-4">
                <Button
                  type="submit"
                  variant="primary"
                  loading={loading}
                  className="flex-1"
                >
                  <FiSave size={18} />
                  Guardar Cambios
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => setEditing(false)}
                  className="flex-1"
                >
                  <FiX size={18} />
                  Cancelar
                </Button>
              </div>
            </form>
          )}
        </div>
      </Card>

      {/* Change Password */}
      <Card>
        <h3 className="text-lg font-bold text-gray-900 mb-4">Cambiar Contraseña</h3>
        <form onSubmit={handleChangePassword} className="space-y-4">
          <Input
            label="Contraseña Actual"
            type="password"
            name="oldPassword"
            value={passwordData.oldPassword}
            onChange={handlePasswordChange}
            error={errors.oldPassword}
          />

          <Input
            label="Nueva Contraseña"
            type="password"
            name="newPassword"
            value={passwordData.newPassword}
            onChange={handlePasswordChange}
            error={errors.newPassword}
          />

          <Input
            label="Confirmar Nueva Contraseña"
            type="password"
            name="confirmPassword"
            value={passwordData.confirmPassword}
            onChange={handlePasswordChange}
            error={errors.confirmPassword}
          />

          <Button
            type="submit"
            variant="primary"
            loading={changing}
            className="w-full"
          >
            Cambiar Contraseña
          </Button>
        </form>
      </Card>

      {/* Account Info */}
      <Card className="bg-gray-50 border border-gray-200">
        <h3 className="text-lg font-bold text-gray-900 mb-4">Información de la Cuenta</h3>
        <div className="space-y-3 text-sm">
          <div className="flex justify-between">
            <span className="text-gray-600">ID de Usuario:</span>
            <span className="font-mono text-gray-900">{user.id}</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">Tipo de Usuario:</span>
            <span className="font-semibold text-gray-900">{user.tipoUsuario}</span>
          </div>
          {user.organizacion && (
            <div className="flex justify-between">
              <span className="text-gray-600">Organización:</span>
              <span className="font-semibold text-gray-900">{user.organizacion}</span>
            </div>
          )}
        </div>
      </Card>
    </div>
  )
}

export default PerfilPage
