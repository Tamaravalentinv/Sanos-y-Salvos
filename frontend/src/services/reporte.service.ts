import api from './api.client'
import { Reporte, Mascota, UbicacionReporte } from '@/types'

// Backend shape
interface BackendMascota {
  id: number
  nombre: string
  tipo: string
  raza: string
  color: string
  caracteristica?: { descripcionFisica?: string; tamaño?: string; senasParticulares?: string }
  fotos?: { urlFoto: string; esPrincipal?: boolean }[]
}

interface BackendReporte {
  id: number
  tipo: 'PERDIDA' | 'ENCONTRADA'
  estado: 'ABIERTO' | 'EN_PROGRESO' | 'RESUELTO' | 'CERRADO'
  usuarioId: number
  mascota: BackendMascota
  ubicacion?: string
  latitud?: number
  longitud?: number
  fechaCreacion?: string
  fechaIncidente?: string
  descripcion?: string
  telefonoContacto?: string
  emailContacto?: string
}

interface CreateReporteInput {
  tipo: string
  descripcion?: string
  fotoBase64?: string
  usuarioId: string
  mascota?: {
    [key: string]: string | undefined
    nombre?: string
    especie?: string
    raza?: string
    color?: string
  }
  ubicacion?: {
    latitud?: number
    longitud?: number
    direccion?: string
    ciudad?: string
    pais?: string
  }
}

function mapTipo(t: string): 'PERDIDO' | 'ENCONTRADO' {
  return t === 'PERDIDA' ? 'PERDIDO' : 'ENCONTRADO'
}

function mapEstado(e: string): 'ACTIVO' | 'RESUELTO' | 'CANCELADO' {
  if (e === 'RESUELTO') return 'RESUELTO'
  if (e === 'CERRADO') return 'CANCELADO'
  return 'ACTIVO'
}

function mapEspecie(tipo: string): 'PERRO' | 'GATO' | 'OTRO' {
  const t = tipo?.toUpperCase() ?? ''
  if (t === 'PERRO') return 'PERRO'
  if (t === 'GATO') return 'GATO'
  return 'OTRO'
}

function mapTamaño(tamanio?: string): 'PEQUEÑO' | 'MEDIANO' | 'GRANDE' {
  const t = tamanio?.toUpperCase() ?? ''
  if (t === 'PEQUEÑO' || t === 'PEQUEÑO') return 'PEQUEÑO'
  if (t === 'GRANDE') return 'GRANDE'
  return 'MEDIANO'
}

function mapReporte(r: BackendReporte): Reporte {
  const mascota: Mascota = {
    id: String(r.mascota?.id ?? ''),
    nombre: r.mascota?.nombre ?? 'Sin nombre',
    especie: mapEspecie(r.mascota?.tipo ?? ''),
    raza: r.mascota?.raza ?? '',
    color: r.mascota?.color ?? '',
    tamaño: mapTamaño(r.mascota?.caracteristica?.tamaño),
    señas_particulares: r.mascota?.caracteristica?.senasParticulares ?? r.mascota?.caracteristica?.descripcionFisica,
    fotografia: r.mascota?.fotos?.find(f => f.esPrincipal)?.urlFoto ?? r.mascota?.fotos?.[0]?.urlFoto,
  }

  const ubicacion: UbicacionReporte = {
    id: '',
    latitud: r.latitud ?? 0,
    longitud: r.longitud ?? 0,
    direccion: r.ubicacion ?? '',
    ciudad: r.ubicacion ?? '',
    pais: 'Chile',
  }

  return {
    id: String(r.id),
    titulo: `${r.tipo === 'PERDIDA' ? 'Perdido' : 'Encontrado'}: ${r.mascota?.nombre ?? ''}`,
    descripcion: r.descripcion ?? '',
    tipo: mapTipo(r.tipo),
    estado: mapEstado(r.estado),
    mascota,
    ubicacion,
    usuarioId: String(r.usuarioId),
    fechaReporte: r.fechaCreacion ?? r.fechaIncidente ?? new Date().toISOString(),
  }
}


export const reporteService = {
  getAllReportes: async (filtros?: {
    tipo?: string
    estado?: string
    ciudad?: string
    page?: number
    size?: number
  }): Promise<{ content: Reporte[]; totalElements: number; totalPages: number }> => {
    let endpoint = '/reports'

    if (filtros?.estado === 'ACTIVO' && !filtros?.tipo) {
      endpoint = '/reports/estado/activos'
    } else if (filtros?.tipo === 'PERDIDO' && !filtros?.estado) {
      endpoint = '/reports/tipo/perdidos'
    } else if (filtros?.tipo === 'ENCONTRADO' && !filtros?.estado) {
      endpoint = '/reports/tipo/encontrados'
    }

    const response = await api.get<BackendReporte[]>(endpoint)
    let reportes = response.data.map(mapReporte)

    // Client-side filtering
    if (filtros?.tipo && endpoint === '/reports') {
      reportes = reportes.filter((r) => r.tipo === filtros.tipo)
    }
    if (filtros?.estado && endpoint !== '/reports/estado/activos') {
      reportes = reportes.filter((r) => r.estado === filtros.estado)
    }
    if (filtros?.ciudad) {
      reportes = reportes.filter((r) =>
        r.ubicacion.ciudad?.toLowerCase().includes(filtros.ciudad!.toLowerCase())
      )
    }

    const size = filtros?.size ?? 12
    const page = filtros?.page ?? 0
    const start = page * size
    const paged = reportes.slice(start, start + size)

    return { content: paged, totalElements: reportes.length, totalPages: Math.ceil(reportes.length / size) }
  },

  getReporteById: async (id: string): Promise<Reporte> => {
    const response = await api.get<BackendReporte>(`/reports/${id}`)
    return mapReporte(response.data)
  },

  createReporte: async (data: CreateReporteInput): Promise<Reporte> => {
    const body = {
      tipoReporte: data.tipo === 'PERDIDO' ? 'PERDIDA' : 'ENCONTRADA',
      usuarioId: parseInt(data.usuarioId) || 1,
      nombreMascota: data.mascota?.nombre ?? '',
      tipo: data.mascota?.especie ?? 'PERRO',
      raza: data.mascota?.raza ?? '',
      color: data.mascota?.color ?? '',
      caracteristica: {
        'tamaño': data.mascota?.tamaño ?? 'MEDIANO',
        senasParticulares: data.mascota?.señas_particulares ?? '',
      },
      ubicacion: [data.ubicacion?.ciudad, data.ubicacion?.direccion]
        .filter(Boolean)
        .join(', '),
      latitud: data.ubicacion?.latitud ?? 0,
      longitud: data.ubicacion?.longitud ?? 0,
      descripcion: data.descripcion ?? '',
      emailContacto: `reporte_${Date.now()}@sanos.cl`,
      fotoBase64: data.fotoBase64 ?? null,
    }
    const response = await api.post<BackendReporte>('/reports', body)
    return mapReporte(response.data)
  },

  getReportesByUsuario: async (usuarioId: string): Promise<Reporte[]> => {
    const response = await api.get<BackendReporte[]>(`/reports/usuario/${usuarioId}`)
    return response.data.map(mapReporte)
  },

  resolverReporte: async (id: string): Promise<void> => {
    await api.patch(`/reports/${id}/estado`, null, {
      params: { nuevoEstado: 'RESUELTO' },
    })
  },

  deleteReporte: async (id: string): Promise<void> => {
    await api.delete(`/reports/${id}`)
  },
}
