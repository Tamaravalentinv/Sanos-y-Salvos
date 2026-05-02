import React from 'react'
import { getStatusBadgeClass } from '@/utils/helpers'

interface BadgeProps {
  children: React.ReactNode
  status?: string
  className?: string
}

const Badge: React.FC<BadgeProps> = ({ children, status, className = '' }) => {
  const statusClass = status ? getStatusBadgeClass(status) : 'bg-gray-100 text-gray-800'

  return (
    <span
      className={`inline-block px-3 py-1 rounded-full text-sm font-semibold ${statusClass} ${className}`}
    >
      {children}
    </span>
  )
}

export default Badge
