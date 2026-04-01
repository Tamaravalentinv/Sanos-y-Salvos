package com.sanosysalvos.ms_geolocalizacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import com.sanosysalvos.ms_geolocalizacion.service.HistorialUbicacionService;
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
public class HistorialUbicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialUbicacionService historialService;

    @Autowired
    private ObjectMapper objectMapper;

    private HistorialUbicacion historial;

    @BeforeEach
    public void setUp() {
        historial = new HistorialUbicacion();
        historial.setId(1L);
        historial.setReporteId(100L);
        historial.setTipoEvento("AVISTAMIENTO");
        historial.setLatitud(-34.9011);
        historial.setLongitud(-56.1645);
        historial.setFechaEvento(LocalDateTime.now());
        historial.setDescripcion("Avistamiento confirmado cerca de parque");
    }

    @Test
    public void testRegistrarAvistamiento() throws Exception {
        when(historialService.registrarAvistamiento(anyLong(), anyDouble(), anyDouble(), anyString(), anyLong(), anyString(), anyInt()))
            .thenReturn(historial);

        String requestBody = "{\"reporteId\": 100, \"latitud\": -34.9011, \"longitud\": -56.1645, \"descripcion\": \"Avistamiento\", \"quienReporta\": 1, \"fuenteInformacion\": \"USUARIO\", \"confiabilidad\": 4}";

        mockMvc.perform(post("/historial/avistamiento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoEvento").value("AVISTAMIENTO"))
                .andExpect(jsonPath("$.latitud").value(-34.9011));

        verify(historialService, times(1)).registrarAvistamiento(anyLong(), anyDouble(), anyDouble(), anyString(), anyLong(), anyString(), anyInt());
    }

    @Test
    public void testRegistrarHallazgo() throws Exception {
        historial.setTipoEvento("HALLAZGO");
        when(historialService.registrarHallazgo(anyLong(), anyDouble(), anyDouble(), anyString(), anyLong()))
            .thenReturn(historial);

        String requestBody = "{\"reporteId\": 100, \"latitud\": -34.9011, \"longitud\": -56.1645, \"descripcion\": \"Mascota encontrada\", \"quienReporta\": 1}";

        mockMvc.perform(post("/historial/hallazgo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoEvento").value("HALLAZGO"));

        verify(historialService, times(1)).registrarHallazgo(anyLong(), anyDouble(), anyDouble(), anyString(), anyLong());
    }

    @Test
    public void testObtenerHistorialPorReporte() throws Exception {
        when(historialService.obtenerHistorialPorReporte(100L)).thenReturn(Arrays.asList(historial));

        mockMvc.perform(get("/historial/reporte/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reporteId").value(100))
                .andExpect(jsonPath("$[0].tipoEvento").value("AVISTAMIENTO"));

        verify(historialService, times(1)).obtenerHistorialPorReporte(100L);
    }
}
