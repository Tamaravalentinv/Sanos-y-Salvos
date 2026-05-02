import axios, { AxiosInstance } from 'axios'

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api'

class ApiClient {
  private client: AxiosInstance

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    // Add token to requests
    this.client.interceptors.request.use((config) => {
      const token = localStorage.getItem('token')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    })

    // Handle errors
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          window.location.href = '/login'
        }
        return Promise.reject(error)
      }
    )
  }

  setAuthToken(token: string) {
    this.client.defaults.headers.common['Authorization'] = `Bearer ${token}`
    localStorage.setItem('token', token)
  }

  clearAuthToken() {
    delete this.client.defaults.headers.common['Authorization']
    localStorage.removeItem('token')
  }

  getClient() {
    return this.client
  }
}

export const apiClient = new ApiClient()
export default apiClient.getClient()
