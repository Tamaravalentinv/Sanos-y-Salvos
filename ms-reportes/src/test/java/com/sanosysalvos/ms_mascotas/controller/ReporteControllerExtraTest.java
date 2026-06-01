package com.sanosysalvos.ms_mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_mascotas.model.EstadoReporte;
import com.sanosysalvos.ms_mascotas.model.Mascota;
import com.sanosysalvos.ms_mascotas.model.Reporte;
import com.sanosysalvos.ms_mascotas.model.TipoReporte;
import com.sanosysalvos.ms_mascotas.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
class ReporteControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReporteService reporteService;

    @Test
    void crearReporteConDatosCompletosConstruyeMascotaYFoto() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setId(10L);
        reporte.setTipo(TipoReporte.PERDIDA);

        when(reporteService.crearReporte(eq(TipoReporte.PERDIDA), eq(1L), any(Mascota.class),
                eq("Parque Central"), eq(40.7128), eq(-74.006), any(LocalDateTime.class),
                eq("Perro perdido"), eq("3001234567"), eq("user@example.com")))
            .thenReturn(reporte);

        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "tipoReporte": "PERDIDA",
                      "usuarioId": 1,
                      "nombreMascota": "Rex",
                      "tipo": "PERRO",
                      "raza": "Labrador",
                      "color": "Negro",
                      "caracteristica": {"tamano": "GRANDE", "edadAproximada": 4},
                      "ubicacion": "Parque Central",
                      "latitud": 40.7128,
                      "longitud": -74.006,
                      "fechaIncidente": "2026-05-20T10:15:30",
                      "descripcion": "Perro perdido",
                      "telefonoContacto": "3001234567",
                      "emailContacto": "user@example.com",
                      "fotoBase64": "data:image/png;base64,abc"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void crearReporteRetornaBadRequestCuandoServicioFalla() throws Exception {
        when(reporteService.crearReporte(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenThrow(new IllegalArgumentException("datos invalidos"));

        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "tipoReporte": "PERDIDA",
                      "usuarioId": 1,
                      "nombreMascota": "Rex"
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarCambiarEstadoYEliminarCubrenRamasDeError() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setId(99L);
        reporte.setEstado(EstadoReporte.ABIERTO);

        when(reporteService.actualizarReporte(eq(99L), any(Reporte.class)))
            .thenThrow(new RuntimeException("Reporte no encontrado"));
        doThrow(new RuntimeException("error")).when(reporteService).eliminarReporte(99L);

        mockMvc.perform(put("/reports/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reporte)))
            .andExpect(status().isNotFound());
        mockMvc.perform(patch("/reports/1/estado").param("nuevoEstado", "INVALIDO"))
            .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/reports/99"))
            .andExpect(status().isInternalServerError());
    }
}
