import React from 'react'

interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string
  error?: string
}

const Textarea: React.FC<TextareaProps> = ({ label, error, className = '', id, ...props }) => {
  const textareaId = id || label?.toLowerCase().replace(/\s+/g, '-')
  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label htmlFor={textareaId} className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
          {label}
        </label>
      )}
      <textarea
        id={textareaId}
        className={`
          w-full px-4 py-2.5 rounded-xl border bg-white text-slate-800 text-sm
          placeholder:text-slate-400 resize-none transition duration-150
          focus:outline-none focus:ring-2 focus:ring-primary-400 focus:border-transparent
          ${error ? 'border-red-400' : 'border-slate-200'}
          ${className}
        `.trim()}
        {...props}
      />
      {error && <span className="text-xs text-red-500 font-medium">{error}</span>}
    </div>
  )
}

export default Textarea
