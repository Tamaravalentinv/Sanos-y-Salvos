import React, { useState } from 'react'
import { Outlet, useLocation } from 'react-router-dom'
import Header from './Header'
import Sidebar from './Sidebar'

const Layout: React.FC = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const location = useLocation()

  const isFullWidth =
    location.pathname === '/mapa' || location.pathname === '/notificaciones'

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <Header onMenuClick={() => setSidebarOpen(!sidebarOpen)} />

        {/* Page Content */}
        <main
          className={`flex-1 overflow-auto ${
            isFullWidth ? 'p-0' : 'p-4 md:p-6'
          }`}
        >
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default Layout
