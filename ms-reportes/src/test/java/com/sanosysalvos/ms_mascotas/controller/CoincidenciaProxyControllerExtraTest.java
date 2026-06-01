package com.sanosysalvos.ms_mascotas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoincidenciaProxyController.class)
class CoincidenciaProxyControllerExtraTest {

    private static final String BASE_URL = "http://ms-coincidencias:8082";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void endpointsDeConsultaRestantesDeleganAlMicroservicio() throws Exception {
        when(restTemplate.getForEntity(BASE_URL + "/matches/confirmadas", Object.class))
            .thenReturn(ResponseEntity.ok("[]"));
        when(restTemplate.getForEntity(BASE_URL + "/matches/potenciales?puntajeMinimo=80.0", Object.class))
            .thenReturn(ResponseEntity.ok("[]"));
        when(restTemplate.getForEntity(BASE_URL + "/matches/recientes?diasAtras=3", Object.class))
            .thenReturn(ResponseEntity.ok("[]"));

        mockMvc.perform(get("/matches/confirmadas")).andExpect(status().isOk());
        mockMvc.perform(get("/matches/potenciales").param("puntajeMinimo", "80.0")).andExpect(status().isOk());
        mockMvc.perform(get("/matches/recientes").param("diasAtras", "3")).andExpect(status().isOk());
    }

    @Test
    void endpointsPatchYDeleteDeleganAlMicroservicio() throws Exception {
        when(restTemplate.exchange(eq(BASE_URL + "/matches/1/confirmar?usuarioId=7"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenReturn(ResponseEntity.ok("ok"));
        when(restTemplate.exchange(eq(BASE_URL + "/matches/1/rechazar?motivo=no coincide"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenReturn(ResponseEntity.ok("ok"));
        when(restTemplate.exchange(eq(BASE_URL + "/matches/1/resolver"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenReturn(ResponseEntity.ok("ok"));

        mockMvc.perform(patch("/matches/1/confirmar").param("usuarioId", "7")).andExpect(status().isOk());
        mockMvc.perform(patch("/matches/1/rechazar").param("motivo", "no coincide")).andExpect(status().isOk());
        mockMvc.perform(patch("/matches/1/resolver")).andExpect(status().isOk());
        mockMvc.perform(delete("/matches/1")).andExpect(status().isNoContent());

        verify(restTemplate).delete(BASE_URL + "/matches/1");
    }

    @Test
    void erroresRestantesDelProxyRetornanInternalServerError() throws Exception {
        when(restTemplate.getForEntity(BASE_URL + "/matches/confirmadas", Object.class))
            .thenThrow(new RuntimeException("down"));
        when(restTemplate.getForEntity(BASE_URL + "/matches/potenciales?puntajeMinimo=70.0", Object.class))
            .thenThrow(new RuntimeException("down"));
        when(restTemplate.getForEntity(BASE_URL + "/matches/recientes?diasAtras=7", Object.class))
            .thenThrow(new RuntimeException("down"));
        when(restTemplate.exchange(eq(BASE_URL + "/matches/2/confirmar?usuarioId=7"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenThrow(new RuntimeException("down"));
        when(restTemplate.exchange(eq(BASE_URL + "/matches/2/rechazar?motivo=no"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenThrow(new RuntimeException("down"));
        when(restTemplate.exchange(eq(BASE_URL + "/matches/2/resolver"),
                eq(HttpMethod.PATCH), any(), eq(Object.class)))
            .thenThrow(new RuntimeException("down"));
        doThrow(new RuntimeException("down")).when(restTemplate).delete(BASE_URL + "/matches/2");

        mockMvc.perform(get("/matches/confirmadas")).andExpect(status().isInternalServerError());
        mockMvc.perform(get("/matches/potenciales")).andExpect(status().isInternalServerError());
        mockMvc.perform(get("/matches/recientes")).andExpect(status().isInternalServerError());
        mockMvc.perform(patch("/matches/2/confirmar").param("usuarioId", "7")).andExpect(status().isInternalServerError());
        mockMvc.perform(patch("/matches/2/rechazar").param("motivo", "no")).andExpect(status().isInternalServerError());
        mockMvc.perform(patch("/matches/2/resolver")).andExpect(status().isInternalServerError());
        mockMvc.perform(delete("/matches/2")).andExpect(status().isInternalServerError());
    }
}
