import { beforeEach, describe, expect, it, vi } from 'vitest'
import { act } from '@testing-library/react'
import { useAuthStore } from '../authStore'
import { useMensajeStore } from '../mensajeStore'
import { useNotificacionStore } from '../notificacionStore'
import { authService } from '@/services/auth.service'
import { notificacionService } from '@/services/notificacion.service'

vi.mock('@/services/auth.service', () => ({
  authService: {
    login: vi.fn(),
    register: vi.fn(),
    logout: vi.fn(),
    getCurrentUser: vi.fn(),
  },
}))

vi.mock('@/services/api.client', () => ({
  apiClient: {
    setAuthToken: vi.fn((token: string) => localStorage.setItem('token', token)),
    clearAuthToken: vi.fn(() => localStorage.removeItem('token')),
  },
}))

vi.mock('@/services/notificacion.service', () => ({
  notificacionService: {
    getNotificaciones: vi.fn(),
    getNotificacionesNoLeidas: vi.fn(),
    marcarComoLeida: vi.fn(),
    deleteNotificacion: vi.fn(),
  },
}))

const user = {
  id: '1',
  nombre: 'Test',
  apellido: 'User',
  email: 'test@example.com',
  tipoUsuario: 'CIUDADANO' as const,
}

describe('stores', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    useAuthStore.setState({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,
    })
    useNotificacionStore.setState({
      notificaciones: [],
      noLeidasCount: 0,
      isLoading: false,
      error: null,
    })
    useMensajeStore.setState({ mensajes: [] })
  })

  it('logs in and stores the token and user', async () => {
    vi.mocked(authService.login).mockResolvedValueOnce({ token: 'abc', user })

    await act(async () => {
      await useAuthStore.getState().login('test@example.com', 'secret')
    })

    expect(useAuthStore.getState().isAuthenticated).toBe(true)
    expect(useAuthStore.getState().user).toEqual(user)
    expect(localStorage.getItem('token')).toBe('abc')
    expect(localStorage.getItem('user')).toContain('test@example.com')
  })

  it('clears local session on logout even when backend fails', async () => {
    localStorage.setItem('token', 'abc')
    localStorage.setItem('user', JSON.stringify(user))
    useAuthStore.setState({ user, token: 'abc', isAuthenticated: true })
    vi.mocked(authService.logout).mockRejectedValueOnce(new Error('offline'))

    await act(async () => {
      await useAuthStore.getState().logout()
    })

    expect(useAuthStore.getState().isAuthenticated).toBe(false)
    expect(localStorage.getItem('token')).toBeNull()
  })

  it('syncs notifications and marks them as read', async () => {
    vi.mocked(notificacionService.getNotificaciones).mockResolvedValueOnce([
      {
        id: '9',
        titulo: 'Aviso',
        mensaje: 'Contenido',
        tipo: 'INFORMACION',
        estado: 'NO_LEIDA',
        usuarioId: '1',
        fechaCreacion: new Date().toISOString(),
        canalEnvio: 'INTERNO',
      },
    ])
    vi.mocked(notificacionService.getNotificacionesNoLeidas).mockResolvedValueOnce([])
    vi.mocked(notificacionService.marcarComoLeida).mockResolvedValueOnce()

    await act(async () => {
      await useNotificacionStore.getState().loadNotificaciones('1')
    })

    expect(useNotificacionStore.getState().notificaciones).toHaveLength(1)

    await act(async () => {
      await useNotificacionStore.getState().marcarComoLeida('9')
    })

    expect(notificacionService.marcarComoLeida).toHaveBeenCalledWith('9')
    expect(useNotificacionStore.getState().notificaciones[0].estado).toBe('LEIDA')
  })

  it('sends messages and counts unread messages', () => {
    act(() => {
      useMensajeStore.getState().enviarMensaje({
        fromUserId: '1',
        fromUserName: 'Test User',
        toUserId: '2',
        toUserName: 'Other User',
        reporteId: '10',
        reporteTitulo: 'Reporte',
        contenido: 'Hola',
      })
    })

    const message = useMensajeStore.getState().mensajes[0]
    expect(message.contenido).toBe('Hola')
    expect(useMensajeStore.getState().getNoLeidosCount('2')).toBe(1)

    act(() => {
      useMensajeStore.getState().marcarLeido(message.id)
    })

    expect(useMensajeStore.getState().getNoLeidosCount('2')).toBe(0)
  })
})
