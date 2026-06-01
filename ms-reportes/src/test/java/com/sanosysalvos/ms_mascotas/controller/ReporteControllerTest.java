package com.sanosysalvos.ms_mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_mascotas.model.*;
import com.sanosysalvos.ms_mascotas.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ReporteController Tests")
@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reporte reporte;
    private Mascota mascota;
    private LocalDateTime ahora;

    @BeforeEach
    void setUp() {
        ahora = LocalDateTime.now();

        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Rex");
        mascota.setTipo("PERRO");
        mascota.setRaza("Labrador");
        mascota.setColor("Negro");

        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo(TipoReporte.PERDIDA);
        reporte.setEstado(EstadoReporte.ABIERTO);
        reporte.setUsuarioId(1L);
        reporte.setMascota(mascota);
        reporte.setUbicacion("Parque Central");
        reporte.setLatitud(40.7128);
        reporte.setLongitud(-74.0060);
        reporte.setFechaIncidente(ahora.minusHours(2));
        reporte.setDescripcion("Perro perdido en el parque");
        reporte.setTelefonoContacto("3001234567");
        reporte.setEmailContacto("user@example.com");
        reporte.setFechaCreacion(ahora);
        reporte.setNumVisualizaciones(0);
    }

    @Test
    @DisplayName("GET /reports - Obtener todos los reportes")
    void testObtenerTodos() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerTodos()).thenReturn(reportes);

        mockMvc.perform(get("/reports")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].tipo").value("PERDIDA"));

        verify(reporteService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /reports/{id} - Obtener reporte por ID")
    void testObtenerPorId() throws Exception {
        when(reporteService.obtenerPorId(1L)).thenReturn(Optional.of(reporte));
        doNothing().when(reporteService).incrementarVisualizaciones(1L);

        mockMvc.perform(get("/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.tipo").value("PERDIDA"));

        verify(reporteService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /reports/{id} - Retorna 404 si reporte no existe")
    void testObtenerPorIdNoExiste() throws Exception {
        when(reporteService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/reports/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(reporteService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("GET /reports/tipo/perdidos - Obtener reportes perdidos")
    void testObtenerPerdidos() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPerdidos()).thenReturn(reportes);

        mockMvc.perform(get("/reports/tipo/perdidos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].tipo").value("PERDIDA"));
    }

    @Test
    @DisplayName("GET /reports/tipo/encontrados - Obtener reportes encontrados")
    void testObtenerEncontrados() throws Exception {
        Reporte encontrado = new Reporte();
        encontrado.setId(2L);
        encontrado.setTipo(TipoReporte.ENCONTRADA);
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(encontrado);
        when(reporteService.obtenerEncontrados()).thenReturn(reportes);

        mockMvc.perform(get("/reports/tipo/encontrados")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/estado/activos - Obtener reportes activos")
    void testObtenerActivos() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerActivos()).thenReturn(reportes);

        mockMvc.perform(get("/reports/estado/activos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/usuario/{usuarioId} - Obtener reportes por usuario")
    void testObtenerPorUsuario() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPorUsuario(1L)).thenReturn(reportes);

        mockMvc.perform(get("/reports/usuario/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/organizacion/{organizacionId} - Obtener reportes por organización")
    void testObtenerPorOrganizacion() throws Exception {
        reporte.setOrganizacionId(5L);
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPorOrganizacion(5L)).thenReturn(reportes);

        mockMvc.perform(get("/reports/organizacion/5")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/urgencia - Obtener reportes urgentes")
    void testObtenerUrgentes() throws Exception {
        reporte.setRequiereUrgencia(true);
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPorUrgencia()).thenReturn(reportes);

        mockMvc.perform(get("/reports/urgencia")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/caracteristicas - Obtener por características")
    void testObtenerPorCaracteristicas() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPorCaracteristicas(TipoReporte.PERDIDA, "Negro", "Labrador"))
            .thenReturn(reportes);

        mockMvc.perform(get("/reports/caracteristicas")
                .param("tipo", "PERDIDA")
                .param("color", "Negro")
                .param("raza", "Labrador")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /reports/proximidad - Obtener por proximidad geográfica")
    void testObtenerPorProximidad() throws Exception {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteService.obtenerPorCercaniaGeografica(40.7128, -74.0060)).thenReturn(reportes);

        mockMvc.perform(get("/reports/proximidad")
                .param("latitud", "40.7128")
                .param("longitud", "-74.0060")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("PUT /reports/{id} - Actualizar reporte")
    void testActualizarReporte() throws Exception {
        Reporte actualizado = new Reporte();
        actualizado.setId(1L);
        actualizado.setDescripcion("Descripción actualizada");
        actualizado.setEstado(EstadoReporte.EN_PROGRESO);

        when(reporteService.actualizarReporte(eq(1L), any(Reporte.class))).thenReturn(actualizado);

        mockMvc.perform(put("/reports/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
            .andExpect(status().isOk());

        verify(reporteService, times(1)).actualizarReporte(eq(1L), any(Reporte.class));
    }

    @Test
    @DisplayName("PATCH /reports/{id}/estado - Cambiar estado")
    void testCambiarEstado() throws Exception {
        mockMvc.perform(patch("/reports/1/estado")
                .param("nuevoEstado", "RESUELTO")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(reporteService, times(1)).cambiarEstado(1L, EstadoReporte.RESUELTO);
    }

    @Test
    @DisplayName("DELETE /reports/{id} - Eliminar reporte")
    void testEliminarReporte() throws Exception {
        mockMvc.perform(delete("/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(reporteService, times(1)).eliminarReporte(1L);
    }

    @Test
    @DisplayName("GET /reports - Retorna lista vacía cuando no hay reportes")
    void testObtenerTodosVacio() throws Exception {
        when(reporteService.obtenerTodos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/reports")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}
