import React, { useEffect } from 'react'
import { useAuthStore } from '@/context/authStore'
import { useNotificacionStore } from '@/context/notificacionStore'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import toast, { Toaster } from 'react-hot-toast'

// Pages
import LoginPage from '@/pages/LoginPage'
import RegisterPage from '@/pages/RegisterPage'
import DashboardPage from '@/pages/DashboardPage'
import ReportesPage from '@/pages/ReportesPage'
import CrearReportePage from '@/pages/CrearReportePage'
import CoincidenciasPage from '@/pages/CoincidenciasPage'
import GeolocalizacionPage from '@/pages/GeolocalizacionPage'
import NotificacionesPage from '@/pages/NotificacionesPage'
import PerfilPage from '@/pages/PerfilPage'

// Components
import ProtectedRoute from '@/components/ProtectedRoute'
import Layout from '@/components/Layout'

function App() {
  const { isAuthenticated, loadUserFromStorage } = useAuthStore()
  const { loadNotificaciones, loadNoLeidasCount } = useNotificacionStore()

  useEffect(() => {
    loadUserFromStorage()
  }, [])

  useEffect(() => {
    if (isAuthenticated) {
      loadNotificaciones()
      loadNoLeidasCount()
      
      // Reload notificaciones every 30 seconds
      const interval = setInterval(() => {
        loadNotificaciones()
        loadNoLeidasCount()
      }, 30000)

      return () => clearInterval(interval)
    }
  }, [isAuthenticated])

  return (
    <>
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: '#fff',
            color: '#000',
          },
        }}
      />
      <Router>
        <Routes>
          {/* Auth Routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Protected Routes */}
          <Route element={<ProtectedRoute />}>
            <Route element={<Layout />}>
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/reportes" element={<ReportesPage />} />
              <Route path="/reportes/crear" element={<CrearReportePage />} />
              <Route path="/coincidencias" element={<CoincidenciasPage />} />
              <Route path="/mapa" element={<GeolocalizacionPage />} />
              <Route path="/notificaciones" element={<NotificacionesPage />} />
              <Route path="/perfil" element={<PerfilPage />} />
            </Route>
          </Route>

          {/* Default redirect */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
