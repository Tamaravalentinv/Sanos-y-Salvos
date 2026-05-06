import React from 'react'

interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  label?: string
  error?: string
  options: Array<{ value: string; label: string }>
  placeholder?: string
}

const Select: React.FC<SelectProps> = ({
  label,
  error,
  options,
  placeholder = 'Selecciona una opción',
  className = '',
  id,
  ...props
}) => {
  const selectId = id || label?.toLowerCase().replace(/\s+/g, '-')

  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label htmlFor={selectId} className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
          {label}
        </label>
      )}
      <select
        id={selectId}
        className={`
          w-full px-4 py-2.5 rounded-xl border bg-white text-slate-800 text-sm
          focus:outline-none focus:ring-2 focus:ring-primary-400 focus:border-transparent
          transition duration-150 cursor-pointer
          ${error ? 'border-red-400' : 'border-slate-200'}
          ${className}
        `.trim()}
        {...props}
      >
        <option value="">{placeholder}</option>
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
      {error && <span className="text-xs text-red-500 font-medium">{error}</span>}
    </div>
  )
}

export default Select
