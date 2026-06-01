package com.sanosysalvos.ms_usuarios.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test de Organizacion Model")
class OrganizacionTest {

    private Organizacion organizacion;
    private TipoOrganizacion tipo;

    @BeforeEach
    void setUp() {
        tipo = new TipoOrganizacion();
        tipo.setId(1L);
        tipo.setNombre("ONG");
        tipo.setDescripcion("Organización No Gubernamental");

        organizacion = new Organizacion();
        organizacion.setId(1L);
        organizacion.setNombre("Fundación Test");
        organizacion.setTipo(tipo);
        organizacion.setDescripcion("Fundación de prueba");
        organizacion.setRut("12.345.678-9");
        organizacion.setEstado("ACTIVO");
        organizacion.setEsVerificada(false);
        organizacion.setFechaRegistro(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear organización con constructor sin argumentos")
    void testCrearOrganizacionSinArgumentos() {
        // Act
        Organizacion org = new Organizacion();

        // Assert
        assertNotNull(org);
    }

    @Test
    @DisplayName("Debe obtener y establecer id")
    void testObtenerEstablecerId() {
        // Arrange
        organizacion.setId(2L);

        // Act & Assert
        assertEquals(2L, organizacion.getId());
    }

    @Test
    @DisplayName("Debe obtener y establecer nombre")
    void testObtenerEstablecerNombre() {
        // Arrange
        organizacion.setNombre("Nueva Fundación");

        // Act & Assert
        assertEquals("Nueva Fundación", organizacion.getNombre());
    }

    @Test
    @DisplayName("Debe obtener y establecer tipo")
    void testObtenerEstablecerTipo() {
        // Arrange
        TipoOrganizacion nuevoTipo = new TipoOrganizacion();
        nuevoTipo.setId(2L);
        nuevoTipo.setNombre("EMPRESA");
        organizacion.setTipo(nuevoTipo);

        // Act & Assert
        assertEquals(2L, organizacion.getTipo().getId());
        assertEquals("EMPRESA", organizacion.getTipo().getNombre());
    }

    @Test
    @DisplayName("Debe obtener y establecer descripción")
    void testObtenerEstablecerDescripcion() {
        // Arrange
        organizacion.setDescripcion("Nueva descripción");

        // Act & Assert
        assertEquals("Nueva descripción", organizacion.getDescripcion());
    }

    @Test
    @DisplayName("Debe obtener y establecer RUT")
    void testObtenerEstablecerRut() {
        // Arrange
        organizacion.setRut("98.765.432-1");

        // Act & Assert
        assertEquals("98.765.432-1", organizacion.getRut());
    }

    @Test
    @DisplayName("Debe obtener y establecer estado")
    void testObtenerEstablecerEstado() {
        // Arrange
        organizacion.setEstado("INACTIVO");

        // Act & Assert
        assertEquals("INACTIVO", organizacion.getEstado());
    }

    @Test
    @DisplayName("Debe obtener y establecer verificada")
    void testObtenerEstablecerEsVerificada() {
        // Arrange
        organizacion.setEsVerificada(true);

        // Act & Assert
        assertTrue(organizacion.getEsVerificada());
    }

    @Test
    @DisplayName("Debe obtener y establecer fecha de registro")
    void testObtenerEstablecerFechaRegistro() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();
        organizacion.setFechaRegistro(ahora);

        // Act & Assert
        assertEquals(ahora, organizacion.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe obtener y establecer contacto")
    void testObtenerEstablecerContacto() {
        // Arrange
        Contacto contacto = new Contacto();
        contacto.setTelefono("+56912345678");
        organizacion.setContacto(contacto);

        // Act & Assert
        assertNotNull(organizacion.getContacto());
        assertEquals("+56912345678", organizacion.getContacto().getTelefono());
    }

    @Test
    @DisplayName("Debe verificar valores por defecto")
    void testValoresPorDefecto() {
        // Arrange & Act
        Organizacion org = new Organizacion();

        // Assert
        assertNull(org.getId());
        assertNull(org.getNombre());
        assertFalse(org.getEsVerificada() != null && org.getEsVerificada());
    }

    @Test
    @DisplayName("Debe obtener y establecer sitio web")
    void testObtenerEstablecerSitioWeb() {
        // Arrange
        organizacion.setSitioWeb("www.fundacion.org");

        // Act & Assert
        assertEquals("www.fundacion.org", organizacion.getSitioWeb());
    }
}
