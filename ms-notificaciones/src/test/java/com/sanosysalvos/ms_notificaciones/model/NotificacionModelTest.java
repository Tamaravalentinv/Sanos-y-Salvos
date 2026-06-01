package com.sanosysalvos.ms_notificaciones.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionModelTest {

    @Test
    void notificacionTieneDefaultsDeEstadoFechaEIntentos() {
        Notificacion notificacion = new Notificacion();

        assertEquals("PENDIENTE", notificacion.getEstado());
        assertNotNull(notificacion.getFechaCreacion());
        assertEquals(0, notificacion.getIntentosEnvio());
    }
}
