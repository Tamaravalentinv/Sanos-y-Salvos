package com.sanosysalvos.ms_coincidencias.service;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.model.PuntajeCoincidencia;
import com.sanosysalvos.ms_coincidencias.repository.CoincidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoincidenciaServiceTest {

    @Mock
    private CoincidenciaRepository coincidenciaRepository;

    @InjectMocks
    private CoincidenciaService coincidenciaService;

    private Coincidencia coincidencia;

    @BeforeEach
    public void setUp() {
        coincidencia = new Coincidencia();
        coincidencia.setId(1L);
        coincidencia.setReportePerdidoId(100L);
        coincidencia.setReporteEncontradoId(200L);
        coincidencia.setPuntajeTotal(85.5);
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION);
        coincidencia.setFechaAnalisis(LocalDateTime.now());
    }

    @Test
    public void testCrearCoincidencia() {
        when(coincidenciaRepository.findByReportePerdidoIdAndReporteEncontradoId(100L, 200L)).thenReturn(null);
        when(coincidenciaRepository.save(any(Coincidencia.class))).thenReturn(coincidencia);

        Coincidencia resultado = coincidenciaService.crearCoincidencia(100L, 200L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getReportePerdidoId());
        assertEquals(200L, resultado.getReporteEncontradoId());
        verify(coincidenciaRepository, times(1)).save(any(Coincidencia.class));
    }

    @Test
    public void testCalcularCoincidencia() {
        PuntajeCoincidencia puntaje = coincidenciaService.calcularCoincidencia(100L, 200L);

        assertNotNull(puntaje);
        assertEquals(100.0, puntaje.getEspecie());
        assertTrue(puntaje.calcularPuntajeTotal() > 0);
    }

    @Test
    public void testObtenerPendientes() {
        when(coincidenciaRepository.findByEstadoOrderByPuntaje(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION))
            .thenReturn(Arrays.asList(coincidencia));

        List<Coincidencia> resultado = coincidenciaService.obtenerPendientes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(coincidenciaRepository, times(1)).findByEstadoOrderByPuntaje(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION);
    }
}
