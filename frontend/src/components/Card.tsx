import React from 'react'

interface CardProps {
  children: React.ReactNode
  className?: string
  hover?: boolean
  padding?: 'none' | 'sm' | 'md' | 'lg'
  onClick?: () => void
}

const paddingClass: Record<string, string> = {
  none: '',
  sm:   'p-4',
  md:   'p-5',
  lg:   'p-6',
}

const Card: React.FC<CardProps> = ({
  children,
  className = '',
  hover = false,
  padding = 'md',
  onClick,
}) => (
  <div
    onClick={onClick}
    className={`
      bg-white rounded-2xl border border-slate-100
      shadow-[0_1px_3px_0_rgb(0_0_0/0.07),0_1px_2px_-1px_rgb(0_0_0/0.07)]
      ${hover ? 'hover:shadow-[0_4px_6px_-1px_rgb(0_0_0/0.08),0_2px_4px_-2px_rgb(0_0_0/0.08)] hover:-translate-y-0.5 transition-all duration-200 cursor-pointer' : ''}
      ${paddingClass[padding]}
      ${className}
    `.trim()}
  >
    {children}
  </div>
)

export default Card
