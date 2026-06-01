package com.sanosysalvos.ms_mascotas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reporte Model Tests")
class ReporteTest {

    private Reporte reporte;
    private Mascota mascota;

    @BeforeEach
    void setUp() {
        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Rex");
        mascota.setTipo("PERRO");
        mascota.setRaza("Labrador");
        mascota.setColor("Negro");

        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo(TipoReporte.PERDIDA);
        reporte.setEstado(EstadoReporte.ABIERTO);
        reporte.setUsuarioId(100L);
        reporte.setMascota(mascota);
        reporte.setUbicacion("Parque Central");
        reporte.setLatitud(40.7128);
        reporte.setLongitud(-74.0060);
        reporte.setFechaIncidente(LocalDateTime.now());
        reporte.setDescripcion("Perro perdido");
        reporte.setTelefonoContacto("3001234567");
        reporte.setEmailContacto("user@example.com");
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setNumVisualizaciones(0);
        reporte.setPrioridad(3);
    }

    @Test
    @DisplayName("Debe crear reporte con valores correctos")
    void testCrearReporte() {
        assertNotNull(reporte);
        assertEquals(1L, reporte.getId());
        assertEquals(TipoReporte.PERDIDA, reporte.getTipo());
        assertEquals(EstadoReporte.ABIERTO, reporte.getEstado());
        assertEquals(100L, reporte.getUsuarioId());
    }

    @Test
    @DisplayName("Debe permitir actualizar tipo de reporte")
    void testActualizarTipo() {
        reporte.setTipo(TipoReporte.ENCONTRADA);
        assertEquals(TipoReporte.ENCONTRADA, reporte.getTipo());
    }

    @Test
    @DisplayName("Debe permitir actualizar estado de reporte")
    void testActualizarEstado() {
        reporte.setEstado(EstadoReporte.RESUELTO);
        assertEquals(EstadoReporte.RESUELTO, reporte.getEstado());
    }

    @Test
    @DisplayName("Debe permitir actualizar descripción")
    void testActualizarDescripcion() {
        String nuevaDescripcion = "Perro encontrado en el parque";
        reporte.setDescripcion(nuevaDescripcion);
        assertEquals(nuevaDescripcion, reporte.getDescripcion());
    }

    @Test
    @DisplayName("Debe permitir incrementar visualizaciones")
    void testIncrementarVisualizaciones() {
        reporte.setNumVisualizaciones(0);
        reporte.setNumVisualizaciones(reporte.getNumVisualizaciones() + 1);
        assertEquals(1, reporte.getNumVisualizaciones());
    }

    @Test
    @DisplayName("Debe permitir cambiar prioridad")
    void testCambiarPrioridad() {
        reporte.setPrioridad(5);
        assertEquals(5, reporte.getPrioridad());
    }

    @Test
    @DisplayName("Debe permitir establecer fecha de resolución")
    void testFechaResolucion() {
        LocalDateTime fechaResolucion = LocalDateTime.now();
        reporte.setFechaResolucion(fechaResolucion);
        assertEquals(fechaResolucion, reporte.getFechaResolucion());
    }

    @Test
    @DisplayName("Debe permitir establecer lugar encontrado")
    void testLugarEncontrado() {
        reporte.setLugarEncontrado("Avenida Principal");
        assertEquals("Avenida Principal", reporte.getLugarEncontrado());
    }

    @Test
    @DisplayName("Debe permitir establecer detalles de hallazgo")
    void testDetallesHallazgo() {
        String detalles = "Encontrado con collar rojo";
        reporte.setDetallesHallazgo(detalles);
        assertEquals(detalles, reporte.getDetallesHallazgo());
    }

    @Test
    @DisplayName("Debe permitir establacer requiere urgencia")
    void testRequiereUrgencia() {
        reporte.setRequiereUrgencia(true);
        assertTrue(reporte.getRequiereUrgencia());
    }

    @Test
    @DisplayName("Debe permitir establecer si es cachorro")
    void testEsCachorro() {
        reporte.setEsCachorro(true);
        assertTrue(reporte.getEsCachorro());
    }

    @Test
    @DisplayName("Debe permitir establecer organización")
    void testOrganizacionId() {
        reporte.setOrganizacionId(50L);
        assertEquals(50L, reporte.getOrganizacionId());
    }

    @Test
    @DisplayName("Debe tener mascota asociada")
    void testMascotaAsociada() {
        assertNotNull(reporte.getMascota());
        assertEquals("Rex", reporte.getMascota().getNombre());
    }

    @Test
    @DisplayName("Debe permitir actualizar mascota")
    void testActualizarMascota() {
        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setId(2L);
        nuevaMascota.setNombre("Firulais");
        reporte.setMascota(nuevaMascota);
        assertEquals("Firulais", reporte.getMascota().getNombre());
    }

    @Test
    @DisplayName("Debe validar coordenadas geográficas")
    void testCoordenadasGeograficas() {
        assertTrue(reporte.getLatitud() >= -90 && reporte.getLatitud() <= 90);
        assertTrue(reporte.getLongitud() >= -180 && reporte.getLongitud() <= 180);
    }
}
