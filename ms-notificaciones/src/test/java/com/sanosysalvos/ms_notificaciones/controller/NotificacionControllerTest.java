package com.sanosysalvos.ms_notificaciones.controller;

import com.sanosysalvos.ms_notificaciones.model.Notificacion;
import com.sanosysalvos.ms_notificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionController controller;

    @Test
    void crearNotificacionRespondeCreatedOBadRequest() {
        NotificacionController.CrearNotificacionRequest request = request();
        Notificacion notificacion = notificacion();
        when(notificacionService.crearNotificacion(1L, "EMAIL", "Asunto", "Contenido",
                "mail@test.com", "EVENTO", 2L)).thenReturn(notificacion);

        var creada = controller.crearNotificacion(request);

        assertEquals(HttpStatus.CREATED, creada.getStatusCode());
        assertSame(notificacion, creada.getBody());

        NotificacionController.CrearNotificacionRequest invalida = request();
        when(notificacionService.crearNotificacion(1L, "EMAIL", "Asunto", "Contenido",
                "mail@test.com", "EVENTO", 2L)).thenThrow(new IllegalArgumentException("tipo invalido"));

        var error = controller.crearNotificacion(invalida);

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals("tipo invalido", error.getBody());
    }

    @Test
    void obtenerNotificacionRespondeOkONotFound() {
        Notificacion notificacion = notificacion();
        when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(notificacion));
        when(notificacionService.obtenerPorId(2L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerNotificacion(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerNotificacion(2L).getStatusCode());
    }

    @Test
    void consultasDeleganEnServicio() {
        Notificacion notificacion = notificacion();
        when(notificacionService.obtenerPorUsuario(1L)).thenReturn(List.of(notificacion));
        when(notificacionService.obtenerNoLeidasPorUsuario(1L)).thenReturn(List.of(notificacion));
        when(notificacionService.obtenerPorEstado("PENDIENTE")).thenReturn(List.of(notificacion));
        when(notificacionService.obtenerPorTipo("EMAIL")).thenReturn(List.of(notificacion));
        when(notificacionService.obtenerPendientes()).thenReturn(List.of(notificacion));

        assertEquals(1, controller.obtenerPorUsuario(1L).getBody().size());
        assertEquals(1, controller.obtenerNoLeidasPorUsuario(1L).getBody().size());
        assertEquals(1, controller.obtenerPorEstado("PENDIENTE").getBody().size());
        assertEquals(1, controller.obtenerPorTipo("EMAIL").getBody().size());
        assertEquals(1, controller.obtenerPendientes().getBody().size());
    }

    @Test
    void marcarYEliminarMapeanExitosYErrores() {
        assertEquals(HttpStatus.OK, controller.marcarComoLeida(1L).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarNotificacion(1L).getStatusCode());

        doThrow(new RuntimeException("sin permiso")).when(notificacionService).marcarComoLeida(2L);
        doThrow(new RuntimeException("sin permiso")).when(notificacionService).eliminarNotificacion(2L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.marcarComoLeida(2L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.eliminarNotificacion(2L).getStatusCode());
    }

    private NotificacionController.CrearNotificacionRequest request() {
        NotificacionController.CrearNotificacionRequest request =
                new NotificacionController.CrearNotificacionRequest();
        ReflectionTestUtils.setField(request, "usuarioId", 1L);
        ReflectionTestUtils.setField(request, "tipo", "EMAIL");
        ReflectionTestUtils.setField(request, "asunto", "Asunto");
        ReflectionTestUtils.setField(request, "contenido", "Contenido");
        ReflectionTestUtils.setField(request, "destinatario", "mail@test.com");
        ReflectionTestUtils.setField(request, "eventoTipo", "EVENTO");
        ReflectionTestUtils.setField(request, "eventoId", 2L);
        return request;
    }

    private Notificacion notificacion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setUsuarioId(1L);
        notificacion.setTipo("EMAIL");
        notificacion.setEstado("PENDIENTE");
        return notificacion;
    }
}
