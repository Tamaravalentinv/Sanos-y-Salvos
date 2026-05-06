import React from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { FiMenu, FiBell, FiSearch } from 'react-icons/fi'
import { useAuthStore } from '@/context/authStore'
import { useNotificacionStore } from '@/context/notificacionStore'
import { getInitials } from '@/utils/helpers'

const PAGE_TITLES: Record<string, string> = {
  '/dashboard':      'Dashboard',
  '/reportes':       'Reportes',
  '/reportes/crear': 'Nuevo Reporte',
  '/coincidencias':  'Coincidencias',
  '/mapa':           'Mapa de Incidencias',
  '/notificaciones': 'Notificaciones',
  '/perfil':         'Mi Perfil',
}

interface HeaderProps {
  onMenuClick: () => void
}

const Header: React.FC<HeaderProps> = ({ onMenuClick }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout } = useAuthStore()
  const { noLeidasCount } = useNotificacionStore()

  const title = PAGE_TITLES[location.pathname] ?? 'Sanos y Salvos'

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="bg-white border-b border-slate-100 sticky top-0 z-30 h-16">
      <div className="flex items-center justify-between h-full px-4 md:px-6">
        {/* Left */}
        <div className="flex items-center gap-4">
          <button
            onClick={onMenuClick}
            className="md:hidden p-2 rounded-xl hover:bg-slate-100 text-slate-600 transition"
            aria-label="Abrir menú"
          >
            <FiMenu size={22} />
          </button>
          <h1 className="font-bold text-slate-800 text-lg hidden sm:block">{title}</h1>
        </div>

        {/* Center - search (desktop) */}
        <div className="hidden md:flex flex-1 max-w-sm mx-6">
          <div className="relative w-full">
            <FiSearch size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="search"
              placeholder="Buscar mascotas, reportes…"
              className="w-full pl-9 pr-4 py-2 rounded-xl bg-slate-50 border border-slate-200
                         text-sm text-slate-700 placeholder:text-slate-400
                         focus:outline-none focus:ring-2 focus:ring-primary-400 focus:border-transparent transition"
            />
          </div>
        </div>

        {/* Right */}
        <div className="flex items-center gap-2">
          {/* Notifications */}
          <Link
            to="/notificaciones"
            className="relative p-2.5 rounded-xl hover:bg-slate-100 text-slate-600 transition"
            aria-label="Notificaciones"
          >
            <FiBell size={20} />
            {noLeidasCount > 0 && (
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full ring-2 ring-white" />
            )}
          </Link>

          {/* Avatar */}
          <button
            onClick={handleLogout}
            title="Cerrar sesión"
            className="flex items-center gap-2 pl-2 pr-3 py-1.5 rounded-xl hover:bg-slate-100 transition group"
          >
            <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-primary-500 to-primary-700
                            flex items-center justify-center text-white font-bold text-xs shadow-sm">
              {user ? getInitials(user.nombre, user.apellido) : '?'}
            </div>
            <span className="hidden md:block text-sm font-medium text-slate-700 group-hover:text-slate-900">
              {user?.nombre}
            </span>
          </button>
        </div>
      </div>
    </header>
  )
}

export default Header
