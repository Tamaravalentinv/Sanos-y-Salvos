import axios, { AxiosInstance } from 'axios'
import { API_CONFIG, AUTH_CONFIG } from '@/config'

class ApiClient {
  private client: AxiosInstance

  constructor() {
    this.client = axios.create({
      baseURL: API_CONFIG.baseURL,
      timeout: API_CONFIG.timeout,
      withCredentials: API_CONFIG.withCredentials,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    // Add token to requests
    this.client.interceptors.request.use((config) => {
      const token = localStorage.getItem(AUTH_CONFIG.tokenKey)
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    })

    // Handle errors
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401 && window.location.pathname !== '/login') {
          localStorage.removeItem(AUTH_CONFIG.tokenKey)
          localStorage.removeItem(AUTH_CONFIG.userKey)
          window.location.href = '/login'
        }
        return Promise.reject(error)
      }
    )
  }

  setAuthToken(token: string) {
    this.client.defaults.headers.common['Authorization'] = `Bearer ${token}`
    localStorage.setItem(AUTH_CONFIG.tokenKey, token)
  }

  clearAuthToken() {
    delete this.client.defaults.headers.common['Authorization']
    localStorage.removeItem(AUTH_CONFIG.tokenKey)
  }

  getClient() {
    return this.client
  }
}

export const apiClient = new ApiClient()
export default apiClient.getClient()
