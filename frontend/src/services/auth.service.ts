import api from './api.client'
import { LoginRequest, RegisterRequest, AuthResponse, User } from '@/types'

export const authService = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/usuarios/auth/login', credentials)
    return response.data
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/usuarios/auth/register', data)
    return response.data
  },

  getCurrentUser: async (): Promise<User> => {
    const response = await api.get<User>('/usuarios/auth/me')
    return response.data
  },

  logout: async (): Promise<void> => {
    await api.post('/usuarios/auth/logout')
  },

  updateProfile: async (userId: string, data: Partial<User>): Promise<User> => {
    const response = await api.put<User>(`/usuarios/${userId}`, data)
    return response.data
  },

  changePassword: async (oldPassword: string, newPassword: string): Promise<void> => {
    await api.post('/usuarios/auth/change-password', {
      oldPassword,
      newPassword,
    })
  },

  resetPassword: async (email: string): Promise<void> => {
    await api.post('/usuarios/auth/reset-password', { email })
  },
}
