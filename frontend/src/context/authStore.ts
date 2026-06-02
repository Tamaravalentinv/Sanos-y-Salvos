import { create } from 'zustand'
import { AxiosError } from 'axios'
import { apiClient } from '@/services/api.client'
import { authService } from '@/services/auth.service'
import { AUTH_CONFIG } from '@/config'
import { ApiError, AuthResponse, RegisterRequest, User } from '@/types'

function getStoredUser(): User | null {
  try {
    const userStr = localStorage.getItem(AUTH_CONFIG.userKey)
    return userStr ? JSON.parse(userStr) : null
  } catch {
    return null
  }
}

function getErrorMessage(error: unknown, fallback: string): string {
  const axiosError = error as AxiosError<ApiError>
  return axiosError.response?.data?.message || fallback
}

interface AuthStore {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
  login: (email: string, password: string) => Promise<void>
  register: (data: RegisterRequest) => Promise<void>
  logout: () => Promise<void>
  setUser: (user: User) => void
  setToken: (token: string) => void
  loadUserFromStorage: () => Promise<void>
  clearError: () => void
}

const storedToken = localStorage.getItem(AUTH_CONFIG.tokenKey)
const storedUser = getStoredUser()

export const useAuthStore = create<AuthStore>((set) => ({
  user: storedUser,
  token: storedToken,
  isAuthenticated: Boolean(storedToken && storedUser),
  isLoading: false,
  error: null,

  login: async (email: string, password: string) => {
    set({ isLoading: true, error: null })
    try {
      const response: AuthResponse = await authService.login({ email, password })
      apiClient.setAuthToken(response.token)
      localStorage.setItem(AUTH_CONFIG.userKey, JSON.stringify(response.user))
      set({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
      })
    } catch (error: unknown) {
      set({ error: getErrorMessage(error, 'Error al iniciar sesion'), isLoading: false })
      throw error
    }
  },

  register: async (data: RegisterRequest) => {
    set({ isLoading: true, error: null })
    try {
      const response: AuthResponse = await authService.register(data)
      apiClient.setAuthToken(response.token)
      localStorage.setItem(AUTH_CONFIG.userKey, JSON.stringify(response.user))
      set({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
      })
    } catch (error: unknown) {
      set({ error: getErrorMessage(error, 'Error al registrarse'), isLoading: false })
      throw error
    }
  },

  logout: async () => {
    try {
      await authService.logout()
    } catch {
      // Keep logout reliable even if the session endpoint is unavailable.
    }
    localStorage.removeItem(AUTH_CONFIG.tokenKey)
    localStorage.removeItem(AUTH_CONFIG.userKey)
    apiClient.clearAuthToken()
    set({
      user: null,
      token: null,
      isAuthenticated: false,
      error: null,
    })
  },

  setUser: (user: User) => {
    set({ user, isAuthenticated: true })
    localStorage.setItem(AUTH_CONFIG.userKey, JSON.stringify(user))
  },

  setToken: (token: string) => {
    set({ token, isAuthenticated: true })
    apiClient.setAuthToken(token)
  },

  loadUserFromStorage: async () => {
    set({ isLoading: true })
    try {
      const token = localStorage.getItem(AUTH_CONFIG.tokenKey)
      const user = getStoredUser()
      if (token && user) {
        apiClient.setAuthToken(token)
        const currentUser = await authService.getCurrentUser().catch(() => user)
        localStorage.setItem(AUTH_CONFIG.userKey, JSON.stringify(currentUser))
        set({ user: currentUser, token, isAuthenticated: true, isLoading: false })
        return
      }
    } catch {
      localStorage.removeItem(AUTH_CONFIG.tokenKey)
      localStorage.removeItem(AUTH_CONFIG.userKey)
      apiClient.clearAuthToken()
    }
    set({ user: null, token: null, isAuthenticated: false, isLoading: false })
  },

  clearError: () => set({ error: null }),
}))
