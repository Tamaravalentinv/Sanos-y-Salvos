import { create } from 'zustand'
import { User, AuthResponse } from '@/types'
import { apiClient } from '@/services/api.client'
import { authService } from '@/services/auth.service'

interface AuthStore {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
  
  // Actions
  login: (email: string, password: string) => Promise<void>
  register: (data: any) => Promise<void>
  logout: () => void
  setUser: (user: User) => void
  setToken: (token: string) => void
  loadUserFromStorage: () => Promise<void>
  clearError: () => void
}

export const useAuthStore = create<AuthStore>((set) => ({
  user: null,
  token: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,

  login: async (email: string, password: string) => {
    set({ isLoading: true, error: null })
    try {
      const response: AuthResponse = await authService.login({ email, password })
      apiClient.setAuthToken(response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      set({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
      })
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Error al iniciar sesión'
      set({ error: errorMessage, isLoading: false })
      throw error
    }
  },

  register: async (data: any) => {
    set({ isLoading: true, error: null })
    try {
      const response: AuthResponse = await authService.register(data)
      apiClient.setAuthToken(response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      set({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
      })
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Error al registrarse'
      set({ error: errorMessage, isLoading: false })
      throw error
    }
  },

  logout: () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    apiClient.clearAuthToken()
    set({
      user: null,
      token: null,
      isAuthenticated: false,
    })
  },

  setUser: (user: User) => {
    set({ user, isAuthenticated: true })
    localStorage.setItem('user', JSON.stringify(user))
  },

  setToken: (token: string) => {
    set({ token, isAuthenticated: true })
    apiClient.setAuthToken(token)
  },

  loadUserFromStorage: async () => {
    try {
      const token = localStorage.getItem('token')
      const userStr = localStorage.getItem('user')
      
      if (token && userStr) {
        const user = JSON.parse(userStr)
        apiClient.setAuthToken(token)
        set({
          user,
          token,
          isAuthenticated: true,
        })
        
        // Verify token is still valid
        try {
          const currentUser = await authService.getCurrentUser()
          set({ user: currentUser })
        } catch {
          // Token expired or invalid
          set({ isAuthenticated: false, user: null, token: null })
          localStorage.removeItem('token')
          localStorage.removeItem('user')
        }
      }
    } catch (error) {
      console.error('Error loading user from storage:', error)
    }
  },

  clearError: () => {
    set({ error: null })
  },
}))
