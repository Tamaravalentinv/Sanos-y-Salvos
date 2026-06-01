package com.sanosysalvos.ms_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_usuarios.model.Organizacion;
import com.sanosysalvos.ms_usuarios.model.TipoOrganizacion;
import com.sanosysalvos.ms_usuarios.service.OrganizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("Test de OrganizacionController")
class OrganizacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizacionService organizacionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Organizacion organizacion;
    private TipoOrganizacion tipoOrganizacion;

    @BeforeEach
    void setUp() {
        tipoOrganizacion = new TipoOrganizacion();
        tipoOrganizacion.setId(1L);
        tipoOrganizacion.setNombre("ONG");

        organizacion = new Organizacion();
        organizacion.setId(1L);
        organizacion.setNombre("Fundación Test");
        organizacion.setTipo(tipoOrganizacion);
        organizacion.setDescripcion("Fundación de prueba");
        organizacion.setRut("12.345.678-9");
        organizacion.setEstado("ACTIVO");
        organizacion.setEsVerificada(false);
        organizacion.setFechaRegistro(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear una organización exitosamente")
    void testCrearOrganizacion() throws Exception {
        // Arrange
        when(organizacionService.crearOrganizacion(
            anyString(), anyLong(), anyString()
        )).thenReturn(organizacion);

        String request = "{\"nombre\":\"Fundación Test\",\"tipoId\":1,\"descripcion\":\"Fundación de prueba\"}";

        // Act & Assert
        mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", equalTo("Fundación Test")))
                .andExpect(jsonPath("$.rut", equalTo("12.345.678-9")));

        verify(organizacionService, times(1)).crearOrganizacion(
            "Fundación Test", 1L, "Fundación de prueba");
    }

    @Test
    @DisplayName("Debe retornar error 400 si tipo no existe")
    void testCrearOrganizacionTipoNoExiste() throws Exception {
        // Arrange
        when(organizacionService.crearOrganizacion(
            anyString(), anyLong(), anyString()
        )).thenThrow(new RuntimeException("Tipo de organización no encontrado"));

        String request = "{\"nombre\":\"Fundación Test\",\"tipoId\":99,\"descripcion\":\"Fundación de prueba\"}";

        // Act & Assert
        mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Tipo de organización no encontrado")));
    }

    @Test
    @DisplayName("Debe obtener organización por ID")
    void testObtenerOrganizacionPorId() throws Exception {
        // Arrange
        when(organizacionService.obtenerPorId(1L)).thenReturn(Optional.of(organizacion));

        // Act & Assert
        mockMvc.perform(get("/organizations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.nombre", equalTo("Fundación Test")));

        verify(organizacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 si organización no existe")
    void testObtenerOrganizacionNoEncontrada() throws Exception {
        // Arrange
        when(organizacionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/organizations/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Organización no encontrada")));
    }

    @Test
    @DisplayName("Debe obtener organización por nombre")
    void testObtenerPorNombre() throws Exception {
        // Arrange
        when(organizacionService.obtenerPorNombre("Fundación Test")).thenReturn(Optional.of(organizacion));

        // Act & Assert
        mockMvc.perform(get("/organizations/nombre/Fundación Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", equalTo("Fundación Test")));

        verify(organizacionService, times(1)).obtenerPorNombre("Fundación Test");
    }

    @Test
    @DisplayName("Debe obtener todas las organizaciones")
    void testObtenerTodas() throws Exception {
        // Arrange
        List<Organizacion> organizaciones = Arrays.asList(organizacion);
        when(organizacionService.obtenerTodas()).thenReturn(organizaciones);

        // Act & Assert
        mockMvc.perform(get("/organizations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", equalTo("Fundación Test")));

        verify(organizacionService, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("Debe obtener organizaciones por estado")
    void testObtenerPorEstado() throws Exception {
        // Arrange
        List<Organizacion> organizaciones = Arrays.asList(organizacion);
        when(organizacionService.obtenerPorEstado("ACTIVO")).thenReturn(organizaciones);

        // Act & Assert
        mockMvc.perform(get("/organizations/estado/ACTIVO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", equalTo("ACTIVO")));

        verify(organizacionService, times(1)).obtenerPorEstado("ACTIVO");
    }

    @Test
    @DisplayName("Debe obtener organizaciones por tipo")
    void testObtenerPorTipo() throws Exception {
        // Arrange
        List<Organizacion> organizaciones = Arrays.asList(organizacion);
        when(organizacionService.obtenerPorTipo(1L)).thenReturn(organizaciones);

        // Act & Assert
        mockMvc.perform(get("/organizations/tipo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(organizacionService, times(1)).obtenerPorTipo(1L);
    }

    @Test
    @DisplayName("Debe obtener organizaciones verificadas")
    void testObtenerVerificadas() throws Exception {
        // Arrange
        organizacion.setEsVerificada(true);
        List<Organizacion> organizaciones = Arrays.asList(organizacion);
        when(organizacionService.obtenerVerificadas()).thenReturn(organizaciones);

        // Act & Assert
        mockMvc.perform(get("/organizations/verificadas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].esVerificada", equalTo(true)));

        verify(organizacionService, times(1)).obtenerVerificadas();
    }

    @Test
    @DisplayName("Debe actualizar organización exitosamente")
    void testActualizarOrganizacion() throws Exception {
        // Arrange
        Organizacion orgActualizada = organizacion;
        orgActualizada.setNombre("Nombre Actualizado");
        
        when(organizacionService.actualizarOrganizacion(eq(1L), org.mockito.ArgumentMatchers.any(Organizacion.class)))
            .thenReturn(orgActualizada);

        // Act & Assert
        mockMvc.perform(put("/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", equalTo("Nombre Actualizado")));

        verify(organizacionService, times(1)).actualizarOrganizacion(eq(1L), org.mockito.ArgumentMatchers.any(Organizacion.class));
    }

    @Test
    @DisplayName("Debe verificar una organización")
    void testVerificarOrganizacion() throws Exception {
        // Arrange
        doNothing().when(organizacionService).verificarOrganizacion(1L);

        // Act & Assert
        mockMvc.perform(post("/organizations/1/verificar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Organización verificada exitosamente")));

        verify(organizacionService, times(1)).verificarOrganizacion(1L);
    }

    @Test
    @DisplayName("Debe eliminar una organización")
    void testEliminarOrganizacion() throws Exception {
        // Arrange
        doNothing().when(organizacionService).eliminarOrganizacion(1L);

        // Act & Assert
        mockMvc.perform(delete("/organizations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(organizacionService, times(1)).eliminarOrganizacion(1L);
    }

    @Test
    @DisplayName("Debe retornar lista vacía de organizaciones")
    void testObtenerTodasVacio() throws Exception {
        // Arrange
        when(organizacionService.obtenerTodas()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/organizations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
