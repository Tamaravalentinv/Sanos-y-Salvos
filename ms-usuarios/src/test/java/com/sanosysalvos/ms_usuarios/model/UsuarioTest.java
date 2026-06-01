package com.sanosysalvos.ms_usuarios.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test de Usuario Model")
class UsuarioTest {

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("CIUDADANO");
        rol.setDescripcion("Usuario ciudadano regular");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("encodedPassword");
        usuario.setNombre("Test");
        usuario.setApellido("User");
        usuario.setRol(rol);
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear usuario con constructor sin argumentos")
    void testCrearUsuarioSinArgumentos() {
        // Act
        Usuario u = new Usuario();

        // Assert
        assertNotNull(u);
    }

    @Test
    @DisplayName("Debe obtener y establecer id de usuario")
    void testObtenerEstablecerId() {
        // Arrange
        usuario.setId(2L);

        // Act & Assert
        assertEquals(2L, usuario.getId());
    }

    @Test
    @DisplayName("Debe obtener y establecer username")
    void testObtenerEstablecerUsername() {
        // Arrange
        usuario.setUsername("newusername");

        // Act & Assert
        assertEquals("newusername", usuario.getUsername());
    }

    @Test
    @DisplayName("Debe obtener y establecer email")
    void testObtenerEstablecerEmail() {
        // Arrange
        usuario.setEmail("newemail@test.com");

        // Act & Assert
        assertEquals("newemail@test.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Debe obtener y establecer password")
    void testObtenerEstablecerPassword() {
        // Arrange
        usuario.setPassword("newPassword");

        // Act & Assert
        assertEquals("newPassword", usuario.getPassword());
    }

    @Test
    @DisplayName("Debe obtener y establecer nombre")
    void testObtenerEstablecerNombre() {
        // Arrange
        usuario.setNombre("NewName");

        // Act & Assert
        assertEquals("NewName", usuario.getNombre());
    }

    @Test
    @DisplayName("Debe obtener y establecer apellido")
    void testObtenerEstablecerApellido() {
        // Arrange
        usuario.setApellido("NewLastName");

        // Act & Assert
        assertEquals("NewLastName", usuario.getApellido());
    }

    @Test
    @DisplayName("Debe obtener y establecer rol")
    void testObtenerEstablecerRol() {
        // Arrange
        Rol nuevoRol = new Rol();
        nuevoRol.setId(2L);
        nuevoRol.setNombre("ADMIN");
        usuario.setRol(nuevoRol);

        // Act & Assert
        assertEquals(2L, usuario.getRol().getId());
        assertEquals("ADMIN", usuario.getRol().getNombre());
    }

    @Test
    @DisplayName("Debe obtener y establecer estado")
    void testObtenerEstablecerEstado() {
        // Arrange
        usuario.setEstado("INACTIVO");

        // Act & Assert
        assertEquals("INACTIVO", usuario.getEstado());
    }

    @Test
    @DisplayName("Debe obtener y establecer fecha de creación")
    void testObtenerEstablecerFechaCreacion() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();
        usuario.setFechaCreacion(ahora);

        // Act & Assert
        assertEquals(ahora, usuario.getFechaCreacion());
    }

    @Test
    @DisplayName("Debe obtener y establecer fecha última conexión")
    void testObtenerEstablecerFechaUltimaConexion() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();
        usuario.setFechaUltimaConexion(ahora);

        // Act & Assert
        assertEquals(ahora, usuario.getFechaUltimaConexion());
    }

    @Test
    @DisplayName("Debe obtener y establecer contacto")
    void testObtenerEstablecerContacto() {
        // Arrange
        Contacto contacto = new Contacto();
        contacto.setTelefono("+56912345678");
        usuario.setContacto(contacto);

        // Act & Assert
        assertNotNull(usuario.getContacto());
        assertEquals("+56912345678", usuario.getContacto().getTelefono());
    }

    @Test
    @DisplayName("Debe verificar valores por defecto")
    void testValoresPorDefecto() {
        // Arrange & Act
        Usuario nuevoUsuario = new Usuario();

        // Assert
        assertEquals("ACTIVO", nuevoUsuario.getEstado());
        assertNotNull(nuevoUsuario.getFechaCreacion());
    }

    @Test
    @DisplayName("Debe crear usuario con constructor completo")
    void testCrearUsuarioConConstructorCompleto() {
        // Arrange & Act
        Usuario u = new Usuario(
            1L, "user", "user@test.com", "pass", "Name", "LastName",
            rol, null, "ACTIVO", LocalDateTime.now(), null, null
        );

        // Assert
        assertEquals(1L, u.getId());
        assertEquals("user", u.getUsername());
        assertEquals("user@test.com", u.getEmail());
        assertEquals("ACTIVO", u.getEstado());
    }
}
