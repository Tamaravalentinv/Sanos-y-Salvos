package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import com.sanosysalvos.ms_geolocalizacion.repository.ZonaIncidenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonaIncidenciaServiceTest {

    @Mock
    private ZonaIncidenciaRepository zonaRepository;

    @InjectMocks
    private ZonaIncidenciaService service;

    @Test
    void crearZonaInicializaValoresDeRiesgo() {
        when(zonaRepository.save(any(ZonaIncidencia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ZonaIncidencia zona = service.crearZona("Centro", -33.0, -70.0, 3.0);

        assertEquals("Centro", zona.getNombre());
        assertEquals(0, zona.getNumIncidencias());
        assertEquals("BAJO", zona.getNivelRiesgo());
        assertTrue(zona.getEsActiva());
    }

    @Test
    void consultasDeleganEnRepositorio() {
        ZonaIncidencia zona = new ZonaIncidencia();
        when(zonaRepository.findById(1L)).thenReturn(Optional.of(zona));
        when(zonaRepository.findByNombre("Centro")).thenReturn(Optional.of(zona));
        when(zonaRepository.findAll()).thenReturn(List.of(zona));
        when(zonaRepository.findByEsActiva(true)).thenReturn(List.of(zona));
        when(zonaRepository.findByNivelRiesgo("ALTO")).thenReturn(List.of(zona));
        when(zonaRepository.findZonasAltoRiesgo(5)).thenReturn(List.of(zona));
        when(zonaRepository.findZonasExitosas(0.7)).thenReturn(List.of(zona));

        assertTrue(service.obtenerPorId(1L).isPresent());
        assertTrue(service.obtenerPorNombre("Centro").isPresent());
        assertEquals(1, service.obtenerTodas().size());
        assertEquals(1, service.obtenerActivas().size());
        assertEquals(1, service.obtenerPorNivelRiesgo("ALTO").size());
        assertEquals(1, service.obtenerZonasAltoRiesgo().size());
        assertEquals(1, service.obtenerZonasExitosas().size());
    }

    @Test
    void actualizarIncidenciasRecalculaTasaYRiesgo() {
        ZonaIncidencia zona = new ZonaIncidencia();
        zona.setNumIncidencias(14);
        zona.setNumPerdidas(8);
        zona.setNumEncontradas(4);
        when(zonaRepository.findById(1L)).thenReturn(Optional.of(zona));

        service.actualizarIncidencias(1L, 2, 1);

        assertEquals(15, zona.getNumIncidencias());
        assertEquals(10, zona.getNumPerdidas());
        assertEquals(5, zona.getNumEncontradas());
        assertEquals(0.5, zona.getTasaRecuperacion());
        assertEquals("ALTO", zona.getNivelRiesgo());
        assertNotNull(zona.getFechaUltimaActualizacion());
        verify(zonaRepository).save(zona);
    }

    @Test
    void actualizarIncidenciasCriticasYEliminar() {
        ZonaIncidencia zona = new ZonaIncidencia();
        zona.setNumIncidencias(29);
        zona.setNumPerdidas(0);
        zona.setNumEncontradas(0);
        when(zonaRepository.findById(5L)).thenReturn(Optional.of(zona));

        service.actualizarIncidencias(5L, 0, 0);
        service.eliminarZona(5L);

        assertEquals("CRITICO", zona.getNivelRiesgo());
        verify(zonaRepository).deleteById(5L);
    }
}
