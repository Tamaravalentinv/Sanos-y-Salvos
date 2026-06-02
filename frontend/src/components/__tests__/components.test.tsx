import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import Button from '../Button'
import Input from '../Input'
import ProtectedRoute from '../ProtectedRoute'
import { useAuthStore } from '@/context/authStore'

describe('components', () => {
  it('renders button loading state', () => {
    render(<Button loading>Guardar</Button>)
    expect(screen.getByRole('button')).toBeDisabled()
    expect(screen.getByText(/Cargando/i)).toBeInTheDocument()
  })

  it('renders input errors and helper text', () => {
    const { rerender } = render(
      <Input label="Email" value="" onChange={() => undefined} helperText="Usa tu correo" />
    )
    expect(screen.getByLabelText('Email')).toBeInTheDocument()
    expect(screen.getByText('Usa tu correo')).toBeInTheDocument()

    rerender(<Input label="Email" value="" onChange={() => undefined} error="Email invalido" />)
    expect(screen.getByText('Email invalido')).toBeInTheDocument()
  })

  it('redirects unauthenticated users from protected routes', () => {
    useAuthStore.setState({ isAuthenticated: false, isLoading: false, user: null, token: null })

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <Routes>
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<div>Dashboard</div>} />
          </Route>
          <Route path="/login" element={<div>Login</div>} />
        </Routes>
      </MemoryRouter>
    )

    expect(screen.getByText('Login')).toBeInTheDocument()
  })

  it('renders protected content for authenticated users', () => {
    useAuthStore.setState({ isAuthenticated: true, isLoading: false, token: 'abc' })

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <Routes>
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<div>Dashboard</div>} />
          </Route>
          <Route path="/login" element={<div>Login</div>} />
        </Routes>
      </MemoryRouter>
    )

    expect(screen.getByText('Dashboard')).toBeInTheDocument()
  })
})
