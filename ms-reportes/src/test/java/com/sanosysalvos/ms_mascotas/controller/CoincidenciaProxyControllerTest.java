package com.sanosysalvos.ms_mascotas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("CoincidenciaProxyController Tests")
@WebMvcTest(CoincidenciaProxyController.class)
class CoincidenciaProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://ms-coincidencias:8082";
    private static final String CONTROLLER_PATH = "/matches";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("POST /matches/analyze - Analizar coincidencia")
    void testAnalizarCoincidencia() throws Exception {
        String requestBody = "{\"reportePerdidoId\": 1, \"reporteEncontradoId\": 2}";
        String responseBody = "{\"id\": 1, \"porcentajeCoincidencia\": 85.5}";

        when(restTemplate.postForEntity(
                BASE_URL + "/matches/analyze",
                requestBody,
                Object.class))
            .thenReturn(ResponseEntity.ok(responseBody));

        mockMvc.perform(post(CONTROLLER_PATH + "/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /matches/{id} - Obtener coincidencia por ID")
    void testObtenerPorId() throws Exception {
        String responseBody = "{\"id\": 1, \"porcentajeCoincidencia\": 85.5}";

        when(restTemplate.getForEntity(
                BASE_URL + "/matches/1",
                Object.class))
            .thenReturn(ResponseEntity.ok(responseBody));

        mockMvc.perform(get(CONTROLLER_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /matches/reporte/{reporteId} - Obtener coincidencias por reporte")
    void testObtenerPorReporte() throws Exception {
        String responseBody = "[{\"id\": 1, \"porcentajeCoincidencia\": 85.5}]";

        when(restTemplate.getForEntity(
                BASE_URL + "/matches/reporte/1",
                Object.class))
            .thenReturn(ResponseEntity.ok(responseBody));

        mockMvc.perform(get(CONTROLLER_PATH + "/reporte/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /matches/pendientes - Obtener coincidencias pendientes")
    void testObtenerPendientes() throws Exception {
        String responseBody = "[{\"id\": 1, \"estado\": \"PENDIENTE\"}]";

        when(restTemplate.getForEntity(
                BASE_URL + "/matches/pendientes",
                Object.class))
            .thenReturn(ResponseEntity.ok(responseBody));

        mockMvc.perform(get(CONTROLLER_PATH + "/pendientes")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /matches/{id} - Retorna error si coincidencia no existe")
    void testObtenerPorIdNoExiste() throws Exception {
        when(restTemplate.getForEntity(
                BASE_URL + "/matches/999",
                Object.class))
            .thenThrow(new RuntimeException("Not Found"));

        mockMvc.perform(get(CONTROLLER_PATH + "/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /matches/analyze - Maneja error en análisis")
    void testAnalizarCoincidenciaError() throws Exception {
        String requestBody = "{\"reportePerdidoId\": 1, \"reporteEncontradoId\": 2}";

        when(restTemplate.postForEntity(
                BASE_URL + "/matches/analyze",
                requestBody,
                Object.class))
            .thenThrow(new RuntimeException("Service Error"));

        mockMvc.perform(post(CONTROLLER_PATH + "/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isInternalServerError());
    }
}
