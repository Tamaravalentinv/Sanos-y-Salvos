package com.sanosysalvos.ms_geolocalizacion.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GeolocalizacionModelTest {

    @Test
    void modelosTienenDefaultsUtiles() {
        Ubicacion ubicacion = new Ubicacion();
        HistorialUbicacion historial = new HistorialUbicacion();
        ZonaIncidencia zona = new ZonaIncidencia();

        assertNotNull(ubicacion.getFechaRegistro());
        assertNotNull(historial.getFechaEvento());
        assertFalse(historial.getComprobado());
        assertEquals(0, zona.getNumIncidencias());
        assertEquals(0.0, zona.getTasaRecuperacion());
        assertEquals("BAJO", zona.getNivelRiesgo());
        assertTrue(zona.getFechaUltimaActualizacion().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
