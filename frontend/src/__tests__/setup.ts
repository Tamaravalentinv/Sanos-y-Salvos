import '@testing-library/jest-dom/vitest'
import { cleanup } from '@testing-library/react'
import { afterEach } from 'vitest'

afterEach(() => {
  cleanup()
  localStorage.clear()
})

export const mockUser = {
  id: '1',
  email: 'test@example.com',
  nombre: 'Test',
  apellido: 'User',
  tipoUsuario: 'CIUDADANO',
} as const

export const mockToken = 'test-token'
