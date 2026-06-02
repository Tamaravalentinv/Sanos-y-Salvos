import { beforeEach, describe, expect, it, vi } from 'vitest'
import { authService } from '../auth.service'
import { coincidenciaService } from '../coincidencia.service'
import { dashboardService } from '../dashboard.service'
import { notificacionService } from '../notificacion.service'
import api from '../api.client'
import { reporteService } from '../reporte.service'

vi.mock('../api.client', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    patch: vi.fn(),
    delete: vi.fn(),
  },
  apiClient: {
    setAuthToken: vi.fn(),
    clearAuthToken: vi.fn(),
  },
}))

const mockedApi = api as unknown as {
  get: ReturnType<typeof vi.fn>
  post: ReturnType<typeof vi.fn>
  put: ReturnType<typeof vi.fn>
  patch: ReturnType<typeof vi.fn>
  delete: ReturnType<typeof vi.fn>
}

describe('frontend services', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('uses existing auth endpoints', async () => {
    mockedApi.post.mockResolvedValueOnce({ data: { token: 'token', user: { id: '1' } } })
    await authService.login({ email: 'user@test.cl', password: 'secret' })
    expect(mockedApi.post).toHaveBeenCalledWith('/usuarios/auth/login', {
      email: 'user@test.cl',
      password: 'secret',
    })

    mockedApi.get.mockResolvedValueOnce({ data: { id: '1' } })
    await authService.getCurrentUser()
    expect(mockedApi.get).toHaveBeenCalledWith('/usuarios/auth/me')

    mockedApi.post.mockResolvedValueOnce({ data: { token: 'new-token', user: { id: '2' } } })
    await authService.register({
      nombre: 'New',
      apellido: 'User',
      email: 'new@test.cl',
      password: 'secret',
      tipoUsuario: 'CIUDADANO',
    })
    expect(mockedApi.post).toHaveBeenCalledWith('/usuarios/auth/register', expect.any(Object))

    mockedApi.put.mockResolvedValueOnce({ data: { id: '1', nombre: 'Updated' } })
    await authService.updateProfile('1', { nombre: 'Updated' })
    expect(mockedApi.put).toHaveBeenCalledWith('/usuarios/1', { nombre: 'Updated' })

    mockedApi.post.mockResolvedValueOnce({ data: { message: 'ok' } })
    await authService.logout()
    expect(mockedApi.post).toHaveBeenCalledWith('/usuarios/auth/logout')
  })

  it('maps notification endpoints exposed by the gateway', async () => {
    mockedApi.get.mockResolvedValueOnce({
      data: [{
        id: 7,
        usuarioId: 1,
        tipo: 'COINCIDENCIA',
        asunto: 'Match',
        contenido: 'Possible match',
        estado: 'PENDIENTE',
        eventoId: 9,
      }],
    })

    const result = await notificacionService.getNotificaciones('1')
    expect(mockedApi.get).toHaveBeenCalledWith('/notificaciones/user/1')
    expect(result[0]).toMatchObject({
      id: '7',
      titulo: 'Match',
      mensaje: 'Possible match',
      estado: 'NO_LEIDA',
      relatedReporteId: '9',
    })

    await notificacionService.marcarComoLeida('7')
    expect(mockedApi.patch).toHaveBeenCalledWith('/notificaciones/7/leer')
  })

  it('uses the existing BFF endpoint for coincidences', async () => {
    mockedApi.get.mockResolvedValueOnce({
      data: {
        totalCoincidencias: 1,
        coincidencias: [{ id: 'c1', estado: 'CONFIRMADA' }],
      },
    })

    const stats = await coincidenciaService.getEstadisticas('5')
    expect(mockedApi.get).toHaveBeenCalledWith('/bff/coincidencias', { params: { userId: '5' } })
    expect(stats).toEqual({ totalCoincidencias: 1, coincidenciasConfirmadas: 1, tasaExito: 1 })

    mockedApi.get.mockResolvedValueOnce({
      data: {
        grupos: [{
          coincidencias: [{
            id: 'c2',
            reportePerdido: { id: '10' },
            reporteEncontrado: { id: '11' },
          }],
        }],
      },
    })
    const recientes = await coincidenciaService.getCoincidenciasRecientes(1, '5')
    expect(recientes).toHaveLength(1)

    mockedApi.get.mockResolvedValueOnce({
      data: {
        coincidencias: [{
          id: 'c3',
          reportePerdido: { id: '20' },
          reporteEncontrado: { id: '21' },
        }],
      },
    })
    const porReporte = await coincidenciaService.getCoincidenciasPorReporte('21', '5')
    expect(porReporte[0].id).toBe('c3')
  })

  it('derives dashboard stats from reports instead of nonexistent dashboard endpoints', async () => {
    vi.spyOn(reporteService, 'getAllReportes').mockResolvedValueOnce({
      content: [
        { id: '1', estado: 'ACTIVO', usuarioId: '1' },
        { id: '2', estado: 'RESUELTO', usuarioId: '2' },
      ],
      totalElements: 2,
      totalPages: 1,
    } as Awaited<ReturnType<typeof reporteService.getAllReportes>>)

    const stats = await dashboardService.getEstadisticasGlobales()
    expect(stats.totalReportes).toBe(2)
    expect(stats.reportesActivos).toBe(1)
    expect(stats.reportesResueltos).toBe(1)
    expect(stats.usuariosActivos).toBe(2)
  })

  it('derives dashboard activity, cities and user stats', async () => {
    const getAllReportes = vi.spyOn(reporteService, 'getAllReportes').mockResolvedValue({
      content: [
        {
          id: '1',
          estado: 'ACTIVO',
          usuarioId: '1',
          fechaReporte: new Date().toISOString(),
          ubicacion: { ciudad: 'Santiago' },
        },
        {
          id: '2',
          estado: 'RESUELTO',
          usuarioId: '2',
          fechaReporte: new Date().toISOString(),
          ubicacion: { ciudad: 'Valparaiso' },
        },
      ],
      totalElements: 2,
      totalPages: 1,
    } as Awaited<ReturnType<typeof reporteService.getAllReportes>>)

    expect(await dashboardService.getActividadReciente(2)).toHaveLength(2)
    expect((await dashboardService.getTopCiudades(1))[0].total).toBe(1)

    getAllReportes.mockRestore()
    vi.spyOn(reporteService, 'getReportesByUsuario').mockResolvedValueOnce([
      { id: '1', estado: 'ACTIVO', usuarioId: '1' },
    ] as Awaited<ReturnType<typeof reporteService.getReportesByUsuario>>)
    expect((await dashboardService.getEstadisticasUsuario('1')).totalReportes).toBe(1)
  })
})
