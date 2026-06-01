package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import com.sanosysalvos.ms_geolocalizacion.repository.HistorialUbicacionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialUbicacionServiceTest {

    @Mock
    private HistorialUbicacionRepository historialRepository;

    @InjectMocks
    private HistorialUbicacionService service;

    @Test
    void registrarAvistamientoYHallazgoConstruyenEventos() {
        when(historialRepository.save(any(HistorialUbicacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HistorialUbicacion avistamiento = service.registrarAvistamiento(1L, -33.0, -70.0,
                "Visto", 9L, "CIUDADANO", 4);
        HistorialUbicacion hallazgo = service.registrarHallazgo(2L, -34.0, -71.0, "Encontrado", 8L);

        assertEquals("AVISTAMIENTO", avistamiento.getTipoEvento());
        assertEquals("CIUDADANO", avistamiento.getFuenteInformacion());
        assertEquals(4, avistamiento.getConfiabilidad());
        assertEquals("HALLAZGO", hallazgo.getTipoEvento());
        assertEquals("USUARIO", hallazgo.getFuenteInformacion());
        assertEquals(5, hallazgo.getConfiabilidad());
        assertTrue(hallazgo.getComprobado());
    }

    @Test
    void consultasDeleganEnRepositorio() {
        HistorialUbicacion historial = new HistorialUbicacion();
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));
        when(historialRepository.findHistorialPorReporte(2L)).thenReturn(List.of(historial));
        when(historialRepository.findByTipoEvento("AVISTAMIENTO")).thenReturn(List.of(historial));
        when(historialRepository.findByQuienReportaId(3L)).thenReturn(List.of(historial));
        when(historialRepository.findByComprobado(false)).thenReturn(List.of(historial));
        when(historialRepository.findPorConfiabilidad(4)).thenReturn(List.of(historial));

        assertTrue(service.obtenerPorId(1L).isPresent());
        assertEquals(1, service.obtenerHistorialPorReporte(2L).size());
        assertEquals(1, service.obtenerPorTipoEvento("AVISTAMIENTO").size());
        assertEquals(1, service.obtenerPorUsuario(3L).size());
        assertEquals(1, service.obtenerNoComprobados().size());
        assertEquals(1, service.obtenerPorConfiabilidad(4).size());
    }

    @Test
    void recientesCalculaFechaDesdeYVerificarMarcaComprobado() {
        HistorialUbicacion historial = new HistorialUbicacion();
        historial.setComprobado(false);
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));

        service.obtenerRecientes(5);
        service.verificar(1L);
        service.eliminar(1L);

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(historialRepository).findRecientes(captor.capture());
        assertTrue(captor.getValue().isBefore(LocalDateTime.now().minusDays(4)));
        assertTrue(historial.getComprobado());
        verify(historialRepository).save(historial);
        verify(historialRepository).deleteById(1L);
    }
}
