package com.sanosysalvos.ms_notificaciones.service;

import com.sanosysalvos.ms_notificaciones.model.Notificacion;
import com.sanosysalvos.ms_notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // Factory Method Pattern: Crear notificación según el tipo
    public Notificacion crearNotificacion(Long usuarioId, String tipo, String asunto, 
                                         String contenido, String destinatario, 
                                         String eventoTipo, Long eventoId) {
        Notificacion notif = new Notificacion();
        notif.setUsuarioId(usuarioId);
        notif.setTipo(tipo);
        notif.setAsunto(asunto);
        notif.setContenido(contenido);
        notif.setDestinatario(destinatario);
        notif.setEventoTipo(eventoTipo);
        notif.setEventoId(eventoId);
        notif.setEstado("PENDIENTE");
        notif.setFechaCreacion(LocalDateTime.now());
        notif.setIntentosEnvio(0);

        Notificacion guardada = notificacionRepository.save(notif);

        // Intentar enviar según el tipo
        if ("EMAIL".equals(tipo)) {
            enviarPorEmail(guardada);
        } else if ("INTERNA".equals(tipo)) {
            marcarComoEnviada(guardada.getId());
        }

        return guardada;
    }

    private void enviarPorEmail(Notificacion notif) {
        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(notif.getDestinatario());
                message.setSubject(notif.getAsunto());
                message.setText(notif.getContenido());
                mailSender.send(message);
                marcarComoEnviada(notif.getId());
            }
        } catch (Exception e) {
            registrarError(notif.getId(), e.getMessage());
        }
    }

    public Optional<Notificacion> obtenerPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> obtenerNoLeidasPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndEstado(usuarioId, "ENVIADA");
    }

    public List<Notificacion> obtenerPorEstado(String estado) {
        return notificacionRepository.findByEstado(estado);
    }

    public List<Notificacion> obtenerPorTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo);
    }

    public List<Notificacion> obtenerPendientes() {
        return notificacionRepository.findByEstado("PENDIENTE");
    }

    public void marcarComoLeida(Long id) {
        Optional<Notificacion> notif = notificacionRepository.findById(id);
        if (notif.isPresent()) {
            Notificacion n = notif.get();
            n.setEstado("LEIDA");
            n.setFechaLectura(LocalDateTime.now());
            notificacionRepository.save(n);
        }
    }

    private void marcarComoEnviada(Long id) {
        Optional<Notificacion> notif = notificacionRepository.findById(id);
        if (notif.isPresent()) {
            Notificacion n = notif.get();
            n.setEstado("ENVIADA");
            n.setFechaEnvio(LocalDateTime.now());
            notificacionRepository.save(n);
        }
    }

    private void registrarError(Long id, String mensaje) {
        Optional<Notificacion> notif = notificacionRepository.findById(id);
        if (notif.isPresent()) {
            Notificacion n = notif.get();
            n.setEstado("FALLIDA");
            n.setIntentosEnvio(n.getIntentosEnvio() + 1);
            n.setMensajeError(mensaje);
            notificacionRepository.save(n);
        }
    }

    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}
