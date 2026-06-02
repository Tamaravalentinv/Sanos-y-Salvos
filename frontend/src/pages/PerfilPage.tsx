import React, { useEffect, useState } from 'react'
import { FiEdit2, FiMail, FiPhone, FiSave, FiShield, FiUser, FiX } from 'react-icons/fi'
import toast from 'react-hot-toast'
import Button from '@/components/Button'
import Card from '@/components/Card'
import Input from '@/components/Input'
import { useAuthStore } from '@/context/authStore'
import { authService } from '@/services/auth.service'
import { User } from '@/types'
import { getInitials } from '@/utils/helpers'

const TIPO_LABEL: Record<string, string> = {
  CIUDADANO: 'Ciudadano',
  CLINICA: 'Clinica veterinaria',
  REFUGIO: 'Refugio',
  MUNICIPALIDAD: 'Municipalidad',
}

const emptyUser: User = {
  id: '',
  nombre: '',
  apellido: '',
  email: '',
  telefono: '',
  tipoUsuario: 'CIUDADANO',
}

const PerfilPage: React.FC = () => {
  const { user, setUser } = useAuthStore()
  const [editing, setEditing] = useState(false)
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState<User>(user ?? emptyUser)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    if (user) setFormData(user)
  }, [user])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }))
  }

  const validateProfile = () => {
    const nextErrors: Record<string, string> = {}
    if (!formData.nombre.trim()) nextErrors.nombre = 'El nombre es requerido'
    if (!formData.apellido.trim()) nextErrors.apellido = 'El apellido es requerido'
    if (!formData.email.includes('@')) nextErrors.email = 'Email invalido'
    setErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
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
    } catch {
      toast.error('Error al actualizar perfil')
    } finally {
      setLoading(false)
    }
  }

  if (!user) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-500 mx-auto mb-3" />
          <p className="text-slate-500 text-sm">Cargando perfil...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6 fade-in max-w-2xl">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Mi Perfil</h1>
        <p className="text-slate-500 text-sm mt-0.5">Administra tu informacion personal</p>
      </div>

      <Card>
        <div className="flex items-center gap-5 pb-5 border-b border-slate-100">
          <div className="w-20 h-20 bg-gradient-to-br from-primary-500 to-primary-700 text-white rounded-2xl flex items-center justify-center font-bold text-2xl shadow-md flex-shrink-0">
            {getInitials(user.nombre, user.apellido)}
          </div>
          <div className="flex-1 min-w-0">
            <h2 className="text-xl font-bold text-slate-900">{user.nombre} {user.apellido}</h2>
            <p className="text-slate-500 text-sm">{user.email}</p>
            <span className="inline-flex items-center gap-1.5 mt-1.5 bg-primary-50 text-primary-700 text-xs font-semibold px-2.5 py-0.5 rounded-full">
              <FiShield size={11} /> {TIPO_LABEL[user.tipoUsuario] ?? user.tipoUsuario}
            </span>
          </div>
          {!editing && (
            <Button variant="secondary" size="sm" onClick={() => setEditing(true)}>
              <FiEdit2 size={14} /> Editar
            </Button>
          )}
        </div>

        {editing ? (
          <form onSubmit={handleUpdateProfile} className="space-y-4 pt-5">
            <div className="grid grid-cols-2 gap-4">
              <Input label="Nombre" name="nombre" value={formData.nombre}
                onChange={handleChange} error={errors.nombre} icon={<FiUser size={15} />} />
              <Input label="Apellido" name="apellido" value={formData.apellido}
                onChange={handleChange} error={errors.apellido} />
            </div>
            <Input label="Email" type="email" name="email" value={formData.email}
              onChange={handleChange} error={errors.email} icon={<FiMail size={15} />} />
            <Input label="Telefono" type="tel" name="telefono" value={formData.telefono || ''}
              onChange={handleChange} icon={<FiPhone size={15} />} />
            <div className="flex gap-2 pt-2">
              <Button type="submit" variant="primary" loading={loading} className="flex-1">
                <FiSave size={15} /> Guardar cambios
              </Button>
              <Button type="button" variant="secondary" onClick={() => setEditing(false)} className="flex-1">
                <FiX size={15} /> Cancelar
              </Button>
            </div>
          </form>
        ) : (
          <div className="pt-5 grid grid-cols-1 sm:grid-cols-2 gap-4">
            {[
              { icon: <FiUser size={15} />, label: 'Nombre', value: `${user.nombre} ${user.apellido}` },
              { icon: <FiMail size={15} />, label: 'Email', value: user.email },
              { icon: <FiPhone size={15} />, label: 'Telefono', value: user.telefono || '-' },
              { icon: <FiShield size={15} />, label: 'Tipo', value: TIPO_LABEL[user.tipoUsuario] ?? user.tipoUsuario },
            ].map((item) => (
              <div key={item.label} className="flex items-start gap-3 bg-slate-50 rounded-xl px-4 py-3">
                <div className="text-primary-500 mt-0.5">{item.icon}</div>
                <div>
                  <p className="text-xs text-slate-400 font-medium">{item.label}</p>
                  <p className="text-sm font-semibold text-slate-800">{item.value}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>

      <Card className="bg-slate-50 border border-slate-200">
        <h3 className="font-semibold text-slate-700 text-sm mb-3">Informacion de la cuenta</h3>
        <div className="space-y-2 text-sm">
          <div className="flex justify-between">
            <span className="text-slate-500">ID</span>
            <span className="font-mono text-slate-700 text-xs">{user.id}</span>
          </div>
          {user.organizacion && (
            <div className="flex justify-between">
              <span className="text-slate-500">Organizacion</span>
              <span className="font-semibold text-slate-800">{user.organizacion}</span>
            </div>
          )}
        </div>
      </Card>
    </div>
  )
}

export default PerfilPage
