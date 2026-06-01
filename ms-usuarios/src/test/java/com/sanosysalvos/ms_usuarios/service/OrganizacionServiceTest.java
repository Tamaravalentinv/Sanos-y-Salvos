package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Organizacion;
import com.sanosysalvos.ms_usuarios.model.TipoOrganizacion;
import com.sanosysalvos.ms_usuarios.repository.OrganizacionRepository;
import com.sanosysalvos.ms_usuarios.repository.TipoOrganizacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Test de OrganizacionService")
class OrganizacionServiceTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private TipoOrganizacionRepository tipoOrganizacionRepository;

    @InjectMocks
    private OrganizacionService organizacionService;

    private Organizacion organizacion;
    private TipoOrganizacion tipoOrganizacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoOrganizacion = new TipoOrganizacion();
        tipoOrganizacion.setId(1L);
        tipoOrganizacion.setNombre("ONG");
        tipoOrganizacion.setDescripcion("Organización No Gubernamental");

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
    void testCrearOrganizacionExitosamente() {
        // Arrange
        when(tipoOrganizacionRepository.findById(1L)).thenReturn(Optional.of(tipoOrganizacion));
        when(organizacionRepository.save(any(Organizacion.class))).thenReturn(organizacion);

        // Act
        Organizacion resultado = organizacionService.crearOrganizacion("Fundación Test", 1L, "Fundación de prueba");

        // Assert
        assertNotNull(resultado);
        assertEquals("Fundación Test", resultado.getNombre());
        assertEquals("ACTIVO", resultado.getEstado());
        assertFalse(resultado.getEsVerificada());
        verify(tipoOrganizacionRepository, times(1)).findById(1L);
        verify(organizacionRepository, times(1)).save(any(Organizacion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si tipo de organización no existe")
    void testCrearOrganizacionTipoNoEncontrado() {
        // Arrange
        when(tipoOrganizacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> organizacionService.crearOrganizacion("Fundación Test", 99L, "Fundación de prueba"));
        
        assertEquals("Tipo de organización no encontrado", exception.getMessage());
        verify(organizacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener organización por ID")
    void testObtenerPorId() {
        // Arrange
        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(organizacion));

        // Act
        Optional<Organizacion> resultado = organizacionService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Fundación Test", resultado.get().getNombre());
        verify(organizacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si organización no existe")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Organizacion> resultado = organizacionService.obtenerPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe obtener organización por nombre")
    void testObtenerPorNombre() {
        // Arrange
        when(organizacionRepository.findByNombre("Fundación Test")).thenReturn(Optional.of(organizacion));

        // Act
        Optional<Organizacion> resultado = organizacionService.obtenerPorNombre("Fundación Test");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Fundación Test", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Debe obtener organización por RUT")
    void testObtenerPorRut() {
        // Arrange
        when(organizacionRepository.findByRut("12.345.678-9")).thenReturn(Optional.of(organizacion));

        // Act
        Optional<Organizacion> resultado = organizacionService.obtenerPorRut("12.345.678-9");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("12.345.678-9", resultado.get().getRut());
    }

    @Test
    @DisplayName("Debe obtener todas las organizaciones")
    void testObtenerTodas() {
        // Arrange
        Organizacion org2 = new Organizacion();
        org2.setId(2L);
        org2.setNombre("Otra Fundación");
        List<Organizacion> organizaciones = Arrays.asList(organizacion, org2);
        when(organizacionRepository.findAll()).thenReturn(organizaciones);

        // Act
        List<Organizacion> resultado = organizacionService.obtenerTodas();

        // Assert
        assertEquals(2, resultado.size());
        verify(organizacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener organizaciones por estado")
    void testObtenerPorEstado() {
        // Arrange
        List<Organizacion> orgs = Arrays.asList(organizacion);
        when(organizacionRepository.findByEstado("ACTIVO")).thenReturn(orgs);

        // Act
        List<Organizacion> resultado = organizacionService.obtenerPorEstado("ACTIVO");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getEstado());
    }

    @Test
    @DisplayName("Debe obtener organizaciones por tipo")
    void testObtenerPorTipo() {
        // Arrange
        List<Organizacion> orgs = Arrays.asList(organizacion);
        when(organizacionRepository.findByTipoId(1L)).thenReturn(orgs);

        // Act
        List<Organizacion> resultado = organizacionService.obtenerPorTipo(1L);

        // Assert
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe obtener organizaciones verificadas")
    void testObtenerVerificadas() {
        // Arrange
        organizacion.setEsVerificada(true);
        List<Organizacion> orgs = Arrays.asList(organizacion);
        when(organizacionRepository.findByEsVerificada(true)).thenReturn(orgs);

        // Act
        List<Organizacion> resultado = organizacionService.obtenerVerificadas();

        // Assert
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getEsVerificada());
    }

    @Test
    @DisplayName("Debe actualizar organización exitosamente")
    void testActualizarOrganizacion() {
        // Arrange
        Organizacion orgActualizada = new Organizacion();
        orgActualizada.setNombre("Nombre Actualizado");
        orgActualizada.setDescripcion("Descripción actualizada");
        
        organizacion.setNombre("Nombre Actualizado");
        organizacion.setDescripcion("Descripción actualizada");

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(organizacion));
        when(organizacionRepository.save(any(Organizacion.class))).thenReturn(organizacion);

        // Act
        Organizacion resultado = organizacionService.actualizarOrganizacion(1L, orgActualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nombre Actualizado", resultado.getNombre());
        verify(organizacionRepository, times(1)).findById(1L);
        verify(organizacionRepository, times(1)).save(any(Organizacion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar organización inexistente")
    void testActualizarOrganizacionNoEncontrada() {
        // Arrange
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> organizacionService.actualizarOrganizacion(99L, organizacion));
        
        assertEquals("Organización no encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Debe obtener lista vacía de organizaciones")
    void testObtenerTodasVacio() {
        // Arrange
        when(organizacionRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Organizacion> resultado = organizacionService.obtenerTodas();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe verificar una organización exitosamente")
    void testVerificarOrganizacion() {
        // Arrange
        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(organizacion));
        when(organizacionRepository.save(any(Organizacion.class))).thenReturn(organizacion);

        // Act
        organizacionService.verificarOrganizacion(1L);

        // Assert
        verify(organizacionRepository, times(1)).findById(1L);
        verify(organizacionRepository, times(1)).save(any(Organizacion.class));
    }

    @Test
    @DisplayName("Debe eliminar una organización exitosamente")
    void testEliminarOrganizacion() {
        // Arrange
        doNothing().when(organizacionRepository).deleteById(1L);

        // Act
        organizacionService.eliminarOrganizacion(1L);

        // Assert
        verify(organizacionRepository, times(1)).deleteById(1L);
    }
}
