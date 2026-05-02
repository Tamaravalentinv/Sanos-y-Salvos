import React from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  helperText?: string
}

const Input: React.FC<InputProps> = ({
  label,
  error,
  helperText,
  className = '',
  id,
  ...props
}) => {
  const inputId = id || label?.toLowerCase().replace(/\s+/g, '-')

  return (
    <div className="flex flex-col gap-2">
      {label && (
        <label
          htmlFor={inputId}
          className="text-sm font-semibold text-gray-700"
        >
          {label}
        </label>
      )}
      <input
        id={inputId}
        className={`px-4 py-2.5 rounded-lg border transition ${
          error
            ? 'border-red-500 focus:border-red-600 focus:ring-red-500'
            : 'border-gray-300 focus:border-primary-500 focus:ring-primary-500'
        } focus:outline-none focus:ring-2 ${className}`}
        {...props}
      />
      {error && <span className="text-sm text-red-600">{error}</span>}
      {helperText && !error && (
        <span className="text-sm text-gray-500">{helperText}</span>
      )}
    </div>
  )
}

export default Input
