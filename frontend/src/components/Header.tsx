import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { FiMenu, FiBell, FiLogOut, FiUser } from 'react-icons/fi'
import { useAuthStore } from '@/context/authStore'
import { useNotificacionStore } from '@/context/notificacionStore'
import { getInitials } from '@/utils/helpers'

interface HeaderProps {
  onMenuClick: () => void
}

const Header: React.FC<HeaderProps> = ({ onMenuClick }) => {
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()
  const { noLeidasCount } = useNotificacionStore()
  const [showProfileMenu, setShowProfileMenu] = React.useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="bg-white border-b border-gray-200 sticky top-0 z-40">
      <div className="flex items-center justify-between px-4 md:px-6 py-4">
        {/* Left side */}
        <div className="flex items-center gap-4">
          <button
            onClick={onMenuClick}
            className="md:hidden p-2 hover:bg-gray-100 rounded-lg transition"
            aria-label="Toggle menu"
          >
            <FiMenu size={24} />
          </button>

          <Link to="/dashboard" className="flex items-center gap-2">
            <div className="flex items-center justify-center w-8 h-8 bg-primary-600 rounded-lg">
              <span className="text-white font-bold text-lg">🐾</span>
            </div>
            <span className="font-bold text-xl text-gray-900 hidden sm:inline">
              Sanos y Salvos
            </span>
          </Link>
        </div>

        {/* Right side */}
        <div className="flex items-center gap-4">
          {/* Notifications */}
          <Link
            to="/notificaciones"
            className="relative p-2 hover:bg-gray-100 rounded-lg transition"
            aria-label="Notificaciones"
          >
            <FiBell size={24} />
            {noLeidasCount > 0 && (
              <span className="absolute top-0 right-0 bg-red-500 text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                {noLeidasCount > 9 ? '9+' : noLeidasCount}
              </span>
            )}
          </Link>

          {/* Profile Menu */}
          <div className="relative">
            <button
              onClick={() => setShowProfileMenu(!showProfileMenu)}
              className="flex items-center gap-2 p-2 hover:bg-gray-100 rounded-lg transition"
            >
              <div className="w-8 h-8 bg-primary-600 text-white rounded-full flex items-center justify-center font-semibold text-sm">
                {user && getInitials(user.nombre, user.apellido)}
              </div>
              <span className="text-sm font-medium text-gray-900 hidden md:inline">
                {user?.nombre}
              </span>
            </button>

            {/* Dropdown Menu */}
            {showProfileMenu && (
              <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 z-50">
                <Link
                  to="/perfil"
                  className="flex items-center gap-2 px-4 py-3 hover:bg-gray-50 transition border-b border-gray-200"
                  onClick={() => setShowProfileMenu(false)}
                >
                  <FiUser size={18} />
                  Mi Perfil
                </Link>
                <button
                  onClick={handleLogout}
                  className="w-full flex items-center gap-2 px-4 py-3 hover:bg-gray-50 transition text-red-600"
                >
                  <FiLogOut size={18} />
                  Cerrar Sesión
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}

export default Header
