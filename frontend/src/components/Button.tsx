import React from 'react'

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'accent' | 'secondary' | 'ghost' | 'danger'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
}

const variantClass: Record<string, string> = {
  primary:   'bg-primary-500 text-white hover:bg-primary-600 active:bg-primary-700 shadow-sm hover:shadow-md',
  accent:    'bg-accent-500 text-white hover:bg-accent-600 active:bg-accent-700 shadow-sm hover:shadow-md',
  secondary: 'bg-white text-slate-700 border border-slate-200 hover:bg-slate-50 hover:border-slate-300 shadow-sm',
  ghost:     'text-slate-600 hover:bg-slate-100 active:bg-slate-200',
  danger:    'bg-red-500 text-white hover:bg-red-600 active:bg-red-700 shadow-sm',
}

const sizeClass: Record<string, string> = {
  sm: 'px-3.5 py-1.5 text-xs rounded-lg gap-1.5',
  md: 'px-5 py-2.5 text-sm rounded-xl gap-2',
  lg: 'px-6 py-3 text-sm rounded-xl gap-2',
}

const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  loading = false,
  disabled,
  children,
  className = '',
  ...props
}) => (
  <button
    disabled={disabled || loading}
    className={`
      inline-flex items-center justify-center font-semibold
      transition-all duration-150 cursor-pointer
      disabled:opacity-50 disabled:cursor-not-allowed
      ${variantClass[variant]} ${sizeClass[size]} ${className}
    `.trim()}
    {...props}
  >
    {loading ? (
      <>
        <span className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin" />
        Cargando…
      </>
    ) : children}
  </button>
)

export default Button
