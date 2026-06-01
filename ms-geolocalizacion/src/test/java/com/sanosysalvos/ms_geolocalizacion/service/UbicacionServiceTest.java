package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.repository.UbicacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService service;

    @Test
    void guardarYRegistrarUbicacionAsignanFechaYCampos() {
        when(ubicacionRepository.save(any(Ubicacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-33.45);
        ubicacion.setLongitud(-70.66);

        Ubicacion guardada = service.guardar(ubicacion);
        Ubicacion reporte = service.registrarUbicacionReporte(5L, -33.4, -70.6, "Parque", "PERDIDA");

        assertNotNull(guardada.getFechaRegistro());
        assertEquals(5L, reporte.getReporteId());
        assertEquals("Parque", reporte.getDescripcion());
        assertEquals("PERDIDA", reporte.getTipoEvento());
    }

    @Test
    void obtenerPorIdNullRetornaVacioSinConsultarRepositorio() {
        assertTrue(service.obtenerPorId(null).isEmpty());
        verify(ubicacionRepository, never()).findById(any());
    }

    @Test
    void consultasDeleganEnRepositorio() {
        Ubicacion ubicacion = new Ubicacion();
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(ubicacionRepository.findAll()).thenReturn(List.of(ubicacion));
        when(ubicacionRepository.findByReporteId(2L)).thenReturn(List.of(ubicacion));
        when(ubicacionRepository.findByComuna("Santiago")).thenReturn(List.of(ubicacion));
        when(ubicacionRepository.findByCiudad("Santiago")).thenReturn(List.of(ubicacion));

        assertTrue(service.obtenerPorId(1L).isPresent());
        assertEquals(1, service.listar().size());
        assertEquals(1, service.obtenerPorReporte(2L).size());
        assertEquals(1, service.obtenerPorComuna("Santiago").size());
        assertEquals(1, service.obtenerPorCiudad("Santiago").size());
    }

    @Test
    void cercaniaConvierteKilometrosAGradosYRecientesCalculaFechaDesde() {
        service.obtenerPorCercaniaGeografica(-33.0, -70.0, 11.1);
        service.obtenerRecientes(3);

        ArgumentCaptor<Double> radioCaptor = ArgumentCaptor.forClass(Double.class);
        verify(ubicacionRepository).findPorCercaniaGeografica(eq(-33.0), eq(-70.0), radioCaptor.capture());
        assertEquals(0.1, radioCaptor.getValue(), 0.0001);
        ArgumentCaptor<LocalDateTime> desdeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(ubicacionRepository).findRecientes(desdeCaptor.capture());
        assertTrue(desdeCaptor.getValue().isBefore(LocalDateTime.now().minusDays(2)));
    }

    @Test
    void actualizarSoloCopiaCamposNoNulosYEliminaSiIdExiste() {
        Ubicacion existente = new Ubicacion();
        existente.setDescripcion("Anterior");
        existente.setLatitud(-33.0);
        existente.setLongitud(-70.0);
        Ubicacion cambios = new Ubicacion();
        cambios.setDescripcion("Nueva");
        cambios.setLongitud(-71.0);
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(existente));

        service.actualizar(1L, cambios);
        service.eliminar(1L);
        service.eliminar(null);

        assertEquals("Nueva", existente.getDescripcion());
        assertEquals(-33.0, existente.getLatitud());
        assertEquals(-71.0, existente.getLongitud());
        verify(ubicacionRepository).save(existente);
        verify(ubicacionRepository).deleteById(1L);
        verify(ubicacionRepository, never()).deleteById(null);
    }

    @Test
    void actualizarRechazaArgumentosNulos() {
        assertThrows(IllegalArgumentException.class, () -> service.actualizar(null, new Ubicacion()));
        assertThrows(IllegalArgumentException.class, () -> service.actualizar(1L, null));
    }
}
