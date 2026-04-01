package com.sanosysalvos.ms_geolocalizacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import com.sanosysalvos.ms_geolocalizacion.service.ZonaIncidenciaService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ZonaIncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZonaIncidenciaService zonaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ZonaIncidencia zona;

    @BeforeEach
    public void setUp() {
        zona = new ZonaIncidencia();
        zona.setId(1L);
        zona.setNombre("Zona Centro");
        zona.setLatitudCentro(-34.9011);
        zona.setLongitudCentro(-56.1645);
        zona.setRadioKm(5.0);
        zona.setNivelRiesgo("ALTO");
        zona.setEsActiva(true);
        zona.setFechaUltimaActualizacion(LocalDateTime.now());
    }

    @Test
    public void testCrearZona() throws Exception {
        when(zonaService.crearZona(anyString(), anyDouble(), anyDouble(), anyDouble())).thenReturn(zona);

        String requestBody = "{\"nombre\": \"Zona Centro\", \"latitudCentro\": -34.9011, \"longitudCentro\": -56.1645, \"radioKm\": 5.0}";

        mockMvc.perform(post("/hotzones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Zona Centro"))
                .andExpect(jsonPath("$.radioKm").value(5.0));

        verify(zonaService, times(1)).crearZona(anyString(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void testObtenerActivas() throws Exception {
        when(zonaService.obtenerActivas()).thenReturn(Arrays.asList(zona));

        mockMvc.perform(get("/hotzones/activas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Zona Centro"))
                .andExpect(jsonPath("$[0].esActiva").value(true));

        verify(zonaService, times(1)).obtenerActivas();
    }

    @Test
    public void testObtenerAltoRiesgo() throws Exception {
        when(zonaService.obtenerZonasAltoRiesgo()).thenReturn(Arrays.asList(zona));

        mockMvc.perform(get("/hotzones/alto-riesgo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nivelRiesgo").value("ALTO"));

        verify(zonaService, times(1)).obtenerZonasAltoRiesgo();
    }
}
