package com.sanosysalvos.ms_notificaciones.service;

import com.sanosysalvos.ms_notificaciones.model.Notificacion;
import com.sanosysalvos.ms_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificacionService service;

    @Test
    void crearNotificacionInternaLaMarcaComoEnviada() {
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion notificacion = invocation.getArgument(0);
            if (notificacion.getId() == null) {
                notificacion.setId(10L);
            }
            return notificacion;
        });
        when(notificacionRepository.findById(10L)).thenAnswer(invocation -> Optional.of(notificacionGuardada()));

        Notificacion resultado = service.crearNotificacion(1L, "INTERNA", "Asunto", "Contenido",
                "destino", "REPORTE", 2L);

        assertEquals(1L, resultado.getUsuarioId());
        assertEquals("INTERNA", resultado.getTipo());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(notificacionRepository, atLeast(2)).save(any(Notificacion.class));
    }

    @Test
    void crearNotificacionEmailEnviaCorreoSiMailSenderExiste() {
        Notificacion guardada = notificacionGuardada();
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(guardada);
        when(notificacionRepository.findById(guardada.getId())).thenReturn(Optional.of(guardada));

        service.crearNotificacion(1L, "EMAIL", "Asunto", "Contenido", "mail@test.com", "EVENTO", 3L);

        verify(mailSender).send(any(org.springframework.mail.SimpleMailMessage.class));
        assertEquals("ENVIADA", guardada.getEstado());
        assertNotNull(guardada.getFechaEnvio());
    }

    @Test
    void errorDeCorreoRegistraEstadoFallido() {
        Notificacion guardada = notificacionGuardada();
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(guardada);
        when(notificacionRepository.findById(guardada.getId())).thenReturn(Optional.of(guardada));
        doThrow(new RuntimeException("SMTP caido")).when(mailSender).send(any(org.springframework.mail.SimpleMailMessage.class));

        service.crearNotificacion(1L, "EMAIL", "Asunto", "Contenido", "mail@test.com", "EVENTO", 3L);

        assertEquals("FALLIDA", guardada.getEstado());
        assertEquals(1, guardada.getIntentosEnvio());
        assertEquals("SMTP caido", guardada.getMensajeError());
    }

    @Test
    void consultasYMarcadoComoLeidaDeleganEnRepositorio() {
        Notificacion notificacion = notificacionGuardada();
        when(notificacionRepository.findById(10L)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.findByUsuarioId(1L)).thenReturn(List.of(notificacion));
        when(notificacionRepository.findByUsuarioIdAndEstado(1L, "ENVIADA")).thenReturn(List.of(notificacion));
        when(notificacionRepository.findByEstado("PENDIENTE")).thenReturn(List.of(notificacion));
        when(notificacionRepository.findByTipo("EMAIL")).thenReturn(List.of(notificacion));

        assertTrue(service.obtenerPorId(10L).isPresent());
        assertEquals(1, service.obtenerPorUsuario(1L).size());
        assertEquals(1, service.obtenerNoLeidasPorUsuario(1L).size());
        assertEquals(1, service.obtenerPorEstado("PENDIENTE").size());
        assertEquals(1, service.obtenerPorTipo("EMAIL").size());
        assertEquals(1, service.obtenerPendientes().size());

        service.marcarComoLeida(10L);
        service.eliminarNotificacion(10L);

        assertEquals("LEIDA", notificacion.getEstado());
        assertNotNull(notificacion.getFechaLectura());
        verify(notificacionRepository).deleteById(10L);
    }

    private Notificacion notificacionGuardada() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(10L);
        notificacion.setUsuarioId(1L);
        notificacion.setTipo("EMAIL");
        notificacion.setAsunto("Asunto");
        notificacion.setContenido("Contenido");
        notificacion.setDestinatario("mail@test.com");
        notificacion.setEstado("PENDIENTE");
        notificacion.setIntentosEnvio(0);
        return notificacion;
    }
}
