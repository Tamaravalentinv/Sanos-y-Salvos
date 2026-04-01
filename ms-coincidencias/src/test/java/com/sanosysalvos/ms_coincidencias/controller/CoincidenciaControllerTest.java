package com.sanosysalvos.ms_coincidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.service.CoincidenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CoincidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoincidenciaService coincidenciaService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testAnalizarCoincidencia() throws Exception {
        when(coincidenciaService.crearCoincidencia(100L, 200L)).thenReturn(coincidencia);

        String requestBody = "{\"reportePerdidoId\": 100, \"reporteEncontradoId\": 200}";

        mockMvc.perform(post("/matches/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.puntajeTotal").value(85.5));

        verify(coincidenciaService, times(1)).crearCoincidencia(100L, 200L);
    }

    @Test
    public void testObtenerPendientes() throws Exception {
        when(coincidenciaService.obtenerPendientes()).thenReturn(Arrays.asList(coincidencia));

        mockMvc.perform(get("/matches/pendientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].puntajeTotal").value(85.5));

        verify(coincidenciaService, times(1)).obtenerPendientes();
    }

    @Test
    public void testConfirmarCoincidencia() throws Exception {
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.CONFIRMADA);
        when(coincidenciaService.confirmarCoincidencia(1L, 50L)).thenReturn(coincidencia);

        mockMvc.perform(patch("/matches/1/confirmar")
                .param("usuarioId", "50")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"));

        verify(coincidenciaService, times(1)).confirmarCoincidencia(1L, 50L);
    }
}
