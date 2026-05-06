import React from 'react'

interface BadgeProps {
  children: React.ReactNode
  status?: string
  className?: string
}

const statusStyles: Record<string, string> = {
  ACTIVO:      'bg-primary-50 text-primary-700 ring-1 ring-primary-200',
  ABIERTO:     'bg-primary-50 text-primary-700 ring-1 ring-primary-200',
  RESUELTO:    'bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
  CERRADO:     'bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
  CANCELADO:   'bg-slate-100 text-slate-600 ring-1 ring-slate-200',
  PERDIDO:     'bg-red-50 text-red-700 ring-1 ring-red-200',
  PERDIDA:     'bg-red-50 text-red-700 ring-1 ring-red-200',
  ENCONTRADO:  'bg-accent-50 text-accent-700 ring-1 ring-accent-200',
  ENCONTRADA:  'bg-accent-50 text-accent-700 ring-1 ring-accent-200',
  CONFIRMADA:  'bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
  RECHAZADA:   'bg-red-50 text-red-700 ring-1 ring-red-200',
  SUGERENCIA:  'bg-accent-50 text-accent-700 ring-1 ring-accent-200',
  LEIDA:       'bg-slate-100 text-slate-500 ring-1 ring-slate-200',
  NO_LEIDA:    'bg-primary-50 text-primary-700 ring-1 ring-primary-200',
}

const Badge: React.FC<BadgeProps> = ({ children, status, className = '' }) => {
  const style = status && statusStyles[status]
    ? statusStyles[status]
    : 'bg-slate-100 text-slate-600 ring-1 ring-slate-200'

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${style} ${className}`}>
      {children}
    </span>
  )
}

export default Badge
