package com.sanosysalvos.ms_usuarios.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test de Rol Model")
class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("CIUDADANO");
        rol.setDescripcion("Usuario ciudadano regular");
    }

    @Test
    @DisplayName("Debe crear rol con constructor sin argumentos")
    void testCrearRolSinArgumentos() {
        // Act
        Rol r = new Rol();

        // Assert
        assertNotNull(r);
    }

    @Test
    @DisplayName("Debe obtener y establecer id del rol")
    void testObtenerEstablecerId() {
        // Arrange
        rol.setId(2L);

        // Act & Assert
        assertEquals(2L, rol.getId());
    }

    @Test
    @DisplayName("Debe obtener y establecer nombre")
    void testObtenerEstablecerNombre() {
        // Arrange
        rol.setNombre("ADMIN");

        // Act & Assert
        assertEquals("ADMIN", rol.getNombre());
    }

    @Test
    @DisplayName("Debe obtener y establecer descripción")
    void testObtenerEstablecerDescripcion() {
        // Arrange
        rol.setDescripcion("Usuario administrador");

        // Act & Assert
        assertEquals("Usuario administrador", rol.getDescripcion());
    }

    @Test
    @DisplayName("Debe crear rol con constructor completo")
    void testCrearRolConConstructorCompleto() {
        // Arrange & Act
        Rol r = new Rol("COORDINADOR", "Usuario coordinador");

        // Assert
        assertEquals("COORDINADOR", r.getNombre());
        assertEquals("Usuario coordinador", r.getDescripcion());
    }

    @Test
    @DisplayName("Debe verificar rol es distinto a otro")
    void testRolesDistintos() {
        // Arrange
        Rol rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("ADMIN");

        // Act & Assert
        assertNotEquals(rol.getId(), rol2.getId());
        assertNotEquals(rol.getNombre(), rol2.getNombre());
    }

    @Test
    @DisplayName("Debe crear rol con constructor vacío y setear valores")
    void testCrearRolVacioYSetearValores() {
        // Arrange & Act
        Rol r = new Rol();
        r.setId(5L);
        r.setNombre("SUPERVISOR");
        r.setDescripcion("Usuario supervisor");

        // Assert
        assertEquals(5L, r.getId());
        assertEquals("SUPERVISOR", r.getNombre());
        assertEquals("Usuario supervisor", r.getDescripcion());
    }

    @Test
    @DisplayName("Debe obtener rol con valores nulos")
    void testRolConValoresNulos() {
        // Arrange & Act
        Rol r = new Rol();

        // Assert
        assertNull(r.getId());
        assertNull(r.getNombre());
        assertNull(r.getDescripcion());
    }
}
