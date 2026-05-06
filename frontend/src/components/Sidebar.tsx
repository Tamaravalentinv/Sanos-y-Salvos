import React from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import {
  FiHome, FiFileText, FiGitMerge, FiMapPin,
  FiBell, FiUser, FiLogOut, FiX, FiPlus,
} from 'react-icons/fi'
import { useAuthStore } from '@/context/authStore'
import { useNotificacionStore } from '@/context/notificacionStore'
import { getInitials } from '@/utils/helpers'

interface SidebarProps {
  open: boolean
  onClose: () => void
}

const NAV = [
  { path: '/dashboard',     label: 'Dashboard',      icon: FiHome },
  { path: '/reportes',      label: 'Reportes',       icon: FiFileText },
  { path: '/coincidencias', label: 'Coincidencias',  icon: FiGitMerge },
  { path: '/mapa',          label: 'Mapa',           icon: FiMapPin },
  { path: '/notificaciones',label: 'Notificaciones', icon: FiBell },
  { path: '/perfil',        label: 'Mi Perfil',      icon: FiUser },
]

const Sidebar: React.FC<SidebarProps> = ({ open, onClose }) => {
  const location = useLocation()
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()
  const { noLeidasCount } = useNotificacionStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div className="fixed inset-0 bg-black/40 md:hidden z-40 backdrop-blur-sm" onClick={onClose} />
      )}

      <aside className={`
        fixed md:relative flex-shrink-0 w-64 h-full
        bg-gradient-to-b from-primary-950 to-primary-900
        flex flex-col z-50
        transition-transform duration-300 ease-in-out
        ${open ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}
      `}>

        {/* Logo */}
        <div className="flex items-center justify-between px-5 py-5 border-b border-white/10">
          <Link to="/dashboard" className="flex items-center gap-3" onClick={onClose}>
            <div className="w-9 h-9 bg-accent-400 rounded-xl flex items-center justify-center text-lg shadow-lg">
              🐾
            </div>
            <div>
              <p className="text-white font-bold text-sm leading-none">Sanos y Salvos</p>
              <p className="text-primary-400 text-xs mt-0.5">Recupera tu mascota</p>
            </div>
          </Link>
          <button onClick={onClose} className="md:hidden text-white/60 hover:text-white p-1">
            <FiX size={20} />
          </button>
        </div>

        {/* Quick action */}
        <div className="px-4 py-3">
          <Link
            to="/reportes/crear"
            onClick={onClose}
            className="flex items-center justify-center gap-2 w-full py-2.5 rounded-xl
                       bg-accent-500 hover:bg-accent-400 text-white text-sm font-semibold
                       transition-all duration-150 shadow-lg shadow-accent-900/30"
          >
            <FiPlus size={16} />
            Nuevo Reporte
          </Link>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-3 py-2 space-y-0.5 overflow-y-auto">
          {NAV.map(({ path, label, icon: Icon }) => {
            const active = location.pathname === path || location.pathname.startsWith(path + '/')
            const isBell = path === '/notificaciones'
            return (
              <Link
                key={path}
                to={path}
                onClick={onClose}
                className={`
                  flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium
                  transition-all duration-150 relative group
                  ${active
                    ? 'bg-white/15 text-white'
                    : 'text-primary-300 hover:bg-white/8 hover:text-white'}
                `}
              >
                {active && (
                  <span className="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-6 bg-accent-400 rounded-r-full" />
                )}
                <Icon size={18} className={active ? 'text-accent-300' : ''} />
                <span>{label}</span>
                {isBell && noLeidasCount > 0 && (
                  <span className="ml-auto bg-red-500 text-white text-xs font-bold rounded-full min-w-[1.25rem] h-5 flex items-center justify-center px-1">
                    {noLeidasCount > 9 ? '9+' : noLeidasCount}
                  </span>
                )}
              </Link>
            )
          })}
        </nav>

        {/* User profile */}
        <div className="px-4 py-4 border-t border-white/10">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-9 h-9 rounded-xl bg-primary-700 border border-white/10 flex items-center justify-center text-white font-bold text-sm">
              {user ? getInitials(user.nombre, user.apellido) : '?'}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-white text-sm font-semibold truncate">{user?.nombre} {user?.apellido}</p>
              <p className="text-primary-400 text-xs truncate">{user?.email}</p>
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="flex items-center gap-2 w-full px-3 py-2 rounded-xl text-primary-300
                       hover:bg-red-500/20 hover:text-red-400 text-sm font-medium transition-all duration-150"
          >
            <FiLogOut size={16} />
            Cerrar sesión
          </button>
        </div>
      </aside>
    </>
  )
}

export default Sidebar
