import React from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  helperText?: string
  icon?: React.ReactNode
}

const Input: React.FC<InputProps> = ({
  label,
  error,
  helperText,
  icon,
  className = '',
  id,
  ...props
}) => {
  const inputId = id || label?.toLowerCase().replace(/\s+/g, '-')

  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label htmlFor={inputId} className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
          {label}
        </label>
      )}
      <div className="relative">
        {icon && (
          <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">{icon}</span>
        )}
        <input
          id={inputId}
          className={`
            w-full py-2.5 rounded-xl border bg-white text-slate-800 text-sm
            placeholder:text-slate-400 transition duration-150
            focus:outline-none focus:ring-2 focus:ring-primary-400 focus:border-transparent
            ${error ? 'border-red-400' : 'border-slate-200'}
            ${icon ? 'pl-10 pr-4' : 'px-4'}
            ${className}
          `.trim()}
          {...props}
        />
      </div>
      {error && <span className="text-xs text-red-500 font-medium">{error}</span>}
      {helperText && !error && <span className="text-xs text-slate-400">{helperText}</span>}
    </div>
  )
}

export default Input
