package com.sanosysalvos.ms_coincidencias.service;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.model.PuntajeCoincidencia;
import com.sanosysalvos.ms_coincidencias.repository.CoincidenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CoincidenciaServiceTest {

    @Mock
    private CoincidenciaRepository coincidenciaRepository;

    @InjectMocks
    private CoincidenciaService service;

    @Test
    void crearCoincidenciaRetornaExistenteSiYaFueCalculada() {
        Coincidencia existente = coincidencia();
        when(coincidenciaRepository.findByReportePerdidoIdAndReporteEncontradoId(1L, 2L))
                .thenReturn(existente);

        Coincidencia resultado = service.crearCoincidencia(1L, 2L);

        assertSame(existente, resultado);
        verify(coincidenciaRepository, never()).save(any());
    }

    @Test
    void crearCoincidenciaNuevaUsaFactoryDePuntajeYEstadoPendiente() {
        when(coincidenciaRepository.findByReportePerdidoIdAndReporteEncontradoId(1L, 2L))
                .thenReturn(null);
        when(coincidenciaRepository.save(any(Coincidencia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Coincidencia resultado = service.crearCoincidencia(1L, 2L);

        assertEquals(1L, resultado.getReportePerdidoId());
        assertEquals(2L, resultado.getReporteEncontradoId());
        assertEquals(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION, resultado.getEstado());
        assertNotNull(resultado.getPuntajeTotal());
        assertNotNull(resultado.getDetallesPuntaje());
        assertNotNull(resultado.getFechaAnalisis());
    }

    @Test
    void calcularCoincidenciaNormalizaPuntaje() {
        PuntajeCoincidencia puntaje = service.calcularCoincidencia(1L, 2L);

        assertEquals(100.0, puntaje.getEspecie());
        assertTrue(puntaje.calcularPuntajeTotal() > 0);
        assertTrue(puntaje.calcularPuntajeTotal() <= 100);
    }

    @Test
    void consultasDeleganEnRepositorio() {
        Coincidencia coincidencia = coincidencia();
        when(coincidenciaRepository.findById(1L)).thenReturn(Optional.of(coincidencia));
        when(coincidenciaRepository.findByReportePerdidoIdAndReporteEncontradoId(1L, 2L)).thenReturn(coincidencia);
        when(coincidenciaRepository.findByReporteId(1L)).thenReturn(List.of(coincidencia));
        when(coincidenciaRepository.findByEstadoOrderByPuntaje(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION))
                .thenReturn(List.of(coincidencia));
        when(coincidenciaRepository.findConfirmedMatches()).thenReturn(List.of(coincidencia));
        when(coincidenciaRepository.findPotentialMatches(70.0)).thenReturn(List.of(coincidencia));

        assertTrue(service.obtenerPorId(1L).isPresent());
        assertTrue(service.obtenerPorId(null).isEmpty());
        assertSame(coincidencia, service.obtenerPorReportes(1L, 2L));
        assertEquals(1, service.obtenerPorReporte(1L).size());
        assertEquals(1, service.obtenerPendientes().size());
        assertEquals(1, service.obtenerConfirmadas().size());
        assertEquals(1, service.obtenerPotenciales(70.0).size());
    }

    @Test
    void obtenerRecientesUsaFechaRelativaYEliminarIgnoraNull() {
        service.obtenerRecientes(2);
        service.eliminarCoincidencia(9L);
        service.eliminarCoincidencia(null);

        verify(coincidenciaRepository).findRecentMatches(any(LocalDateTime.class));
        verify(coincidenciaRepository).deleteById(9L);
        verify(coincidenciaRepository, never()).deleteById(null);
    }

    @Test
    void confirmarRechazarYResolverCambianEstado() {
        Coincidencia paraConfirmar = coincidencia();
        Coincidencia paraRechazar = coincidencia();
        Coincidencia paraResolver = coincidencia();
        when(coincidenciaRepository.findById(1L))
                .thenReturn(Optional.of(paraConfirmar))
                .thenReturn(Optional.of(paraRechazar))
                .thenReturn(Optional.of(paraResolver));
        when(coincidenciaRepository.save(any(Coincidencia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coincidencia confirmada = service.confirmarCoincidencia(1L, 99L);
        Coincidencia rechazada = service.rechazarCoincidencia(1L, "No coincide");
        Coincidencia resuelta = service.resolverCaso(1L);

        assertEquals(Coincidencia.EstadoCoincidencia.CONFIRMADA, confirmada.getEstado());
        assertEquals(99L, confirmada.getUsuarioQuienConfirmo());
        assertEquals(Coincidencia.EstadoCoincidencia.RECHAZADA, rechazada.getEstado());
        assertEquals("No coincide", rechazada.getMotivoRechazo());
        assertEquals(Coincidencia.EstadoCoincidencia.RESOLVIO_CASO, resuelta.getEstado());
        assertNotNull(resuelta.getFechaResolucion());
    }

    @Test
    void cambiosDeEstadoRechazanIdNuloONoEncontrado() {
        when(coincidenciaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.confirmarCoincidencia(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> service.confirmarCoincidencia(2L, 1L));
        assertThrows(IllegalArgumentException.class, () -> service.rechazarCoincidencia(null, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.resolverCaso(null));
    }

    private Coincidencia coincidencia() {
        Coincidencia coincidencia = new Coincidencia();
        coincidencia.setId(1L);
        coincidencia.setReportePerdidoId(1L);
        coincidencia.setReporteEncontradoId(2L);
        coincidencia.setPuntajeTotal(80.0);
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION);
        coincidencia.setFechaAnalisis(LocalDateTime.now());
        coincidencia.setVersion(0);
        return coincidencia;
    }
}
