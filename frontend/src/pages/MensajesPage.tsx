import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiSend } from 'react-icons/fi'
import Card from '@/components/Card'
import Button from '@/components/Button'
import { useMensajeStore } from '@/context/mensajeStore'
import { useAuthStore } from '@/context/authStore'
import { Mensaje } from '@/types'
import { formatDistanceToNow, formatDate } from '@/utils/helpers'
import toast from 'react-hot-toast'

const MensajesPage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const { mensajes, marcarLeido, enviarMensaje } = useMensajeStore()
  const [tab, setTab] = useState<'recibidos' | 'enviados'>('recibidos')
  const [selected, setSelected] = useState<Mensaje | null>(null)
  const [replyText, setReplyText] = useState('')
  const [sending, setSending] = useState(false)

  const recibidos = mensajes.filter(m => m.toUserId === user?.id)
  const enviados = mensajes.filter(m => m.fromUserId === user?.id)
  const noLeidos = recibidos.filter(m => !m.leido).length
  const lista = tab === 'recibidos' ? recibidos : enviados

  const handleSelect = (m: Mensaje) => {
    setSelected(m)
    if (!m.leido && m.toUserId === user?.id) marcarLeido(m.id)
    setReplyText('')
  }

  const handleReply = async () => {
    if (!selected || !replyText.trim() || !user) return
    setSending(true)
    enviarMensaje({
      fromUserId: user.id,
      fromUserName: `${user.nombre} ${user.apellido}`,
      toUserId: selected.fromUserId,
      toUserName: selected.fromUserName,
      reporteId: selected.reporteId,
      reporteTitulo: selected.reporteTitulo,
      contenido: replyText.trim(),
    })
    setReplyText('')
    toast.success('Respuesta enviada')
    setSending(false)
  }

  const otherName = (m: Mensaje) =>
    tab === 'recibidos' ? m.fromUserName : m.toUserName

  return (
    <div className="space-y-5 fade-in max-w-4xl mx-auto">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Mensajes</h1>
        <p className="text-slate-500 text-sm mt-0.5">
          {noLeidos > 0 ? `${noLeidos} sin leer` : 'Todo al día'}
        </p>
      </div>

      {/* Tabs */}
      <div className="flex gap-1 bg-slate-100 p-1 rounded-xl w-fit">
        {(['recibidos', 'enviados'] as const).map(t => (
          <button
            key={t}
            onClick={() => { setTab(t); setSelected(null) }}
            className={`px-4 py-1.5 rounded-lg text-sm font-semibold transition-all ${
              tab === t ? 'bg-white text-primary-700 shadow-sm' : 'text-slate-500 hover:text-slate-700'
            }`}
          >
            {t === 'recibidos' ? 'Recibidos' : 'Enviados'}
            {t === 'recibidos' && noLeidos > 0 && (
              <span className="ml-1.5 bg-primary-500 text-white text-xs px-1.5 py-0.5 rounded-full">
                {noLeidos}
              </span>
            )}
          </button>
        ))}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* List */}
        <div className="md:col-span-1 space-y-2">
          {lista.length === 0 ? (
            <Card className="text-center py-12">
              <div className="text-5xl mb-3">💬</div>
              <p className="text-slate-500 text-sm">
                {tab === 'recibidos' ? 'Sin mensajes recibidos' : 'No has enviado mensajes aún'}
              </p>
              {tab === 'recibidos' && (
                <p className="text-slate-400 text-xs mt-1">
                  Cuando alguien te contacte por un reporte, aparecerá aquí.
                </p>
              )}
            </Card>
          ) : (
            lista.map(m => (
              <Card
                key={m.id}
                className={`cursor-pointer transition-all hover:shadow-md ${
                  selected?.id === m.id ? 'border-primary-300 bg-primary-50/30' : ''
                } ${!m.leido && tab === 'recibidos' ? 'border-l-4 border-l-primary-400' : ''}`}
                onClick={() => handleSelect(m)}
              >
                <div className="flex gap-3 items-start">
                  <div className="w-9 h-9 rounded-xl bg-primary-100 flex items-center justify-center flex-shrink-0 text-sm font-bold text-primary-700">
                    {otherName(m).charAt(0).toUpperCase()}
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-1.5 mb-0.5">
                      <p className="font-semibold text-slate-800 text-sm truncate">{otherName(m)}</p>
                      {!m.leido && tab === 'recibidos' && (
                        <span className="w-2 h-2 bg-primary-500 rounded-full flex-shrink-0" />
                      )}
                    </div>
                    <p className="text-xs text-primary-500 font-medium truncate">{m.reporteTitulo}</p>
                    <p className="text-xs text-slate-500 truncate mt-0.5">{m.contenido}</p>
                    <p className="text-xs text-slate-400 mt-1">{formatDistanceToNow(m.fechaCreacion)}</p>
                  </div>
                </div>
              </Card>
            ))
          )}
        </div>

        {/* Detail */}
        <div className="md:col-span-2">
          {selected ? (
            <Card className="space-y-4">
              <div className="flex items-start gap-3">
                <div className="w-10 h-10 rounded-xl bg-primary-100 flex items-center justify-center font-bold text-primary-700 flex-shrink-0">
                  {otherName(selected).charAt(0).toUpperCase()}
                </div>
                <div className="flex-1 min-w-0">
                  <p className="font-bold text-slate-800 text-sm">
                    {tab === 'recibidos' ? `De: ${selected.fromUserName}` : `Para: ${selected.toUserName}`}
                  </p>
                  <button
                    onClick={() => navigate(`/reportes/${selected.reporteId}`)}
                    className="text-xs text-primary-500 hover:underline font-medium"
                  >
                    Re: {selected.reporteTitulo} →
                  </button>
                  <p className="text-xs text-slate-400 mt-0.5">{formatDate(selected.fechaCreacion)}</p>
                </div>
              </div>

              <div className="bg-slate-50 rounded-xl p-4">
                <p className="text-slate-700 text-sm leading-relaxed whitespace-pre-wrap">{selected.contenido}</p>
              </div>

              {tab === 'recibidos' && (
                <div className="border-t border-slate-100 pt-4 space-y-3">
                  <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Responder</p>
                  <textarea
                    value={replyText}
                    onChange={e => setReplyText(e.target.value)}
                    placeholder="Escribe tu respuesta…"
                    rows={3}
                    className="w-full border border-slate-200 rounded-xl px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-300 resize-none"
                  />
                  <Button
                    variant="primary"
                    size="sm"
                    onClick={handleReply}
                    loading={sending}
                    disabled={!replyText.trim()}
                  >
                    <FiSend size={14} /> Enviar respuesta
                  </Button>
                </div>
              )}
            </Card>
          ) : (
            <Card className="flex flex-col items-center justify-center py-20 text-center">
              <div className="text-5xl mb-4">💬</div>
              <p className="text-slate-500 text-sm font-medium">Selecciona un mensaje para leerlo</p>
              <p className="text-slate-400 text-xs mt-1">
                Puedes contactar al dueño de un reporte desde la página de detalle
              </p>
            </Card>
          )}
        </div>
      </div>
    </div>
  )
}

export default MensajesPage
