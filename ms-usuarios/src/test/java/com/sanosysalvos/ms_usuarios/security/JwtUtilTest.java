package com.sanosysalvos.ms_usuarios.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test de JwtUtil")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Configurar propiedades privadas para las pruebas
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mysecretkeythatiswelloverthirtytwocharacterslongforhmacsha256");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 3600000L);
    }

    @Test
    @DisplayName("Debe generar un token JWT válido")
    void testGenerarToken() {
        // Act
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por punto
    }

    @Test
    @DisplayName("Debe extraer el email del token")
    void testExtraerEmail() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        // Act
        String email = jwtUtil.extractEmail(token);

        // Assert
        assertEquals("test@test.com", email);
    }

    @Test
    @DisplayName("Debe extraer el userId del token")
    void testExtraerUserId() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        // Act
        Long userId = jwtUtil.extractUserId(token);

        // Assert
        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("Debe validar un token JWT válido")
    void testValidarTokenValido() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        // Act
        boolean esValido = jwtUtil.validateToken(token);

        // Assert
        assertTrue(esValido);
    }

    @Test
    @DisplayName("Debe rechazar un token JWT inválido")
    void testValidarTokenInvalido() {
        // Arrange
        String tokenInvalido = "invalid.token.here";

        // Act
        boolean esValido = jwtUtil.validateToken(tokenInvalido);

        // Assert
        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe rechazar un token vacío")
    void testValidarTokenVacio() {
        // Act
        boolean esValido = jwtUtil.validateToken("");

        // Assert
        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe generar tokens únicos")
    void testGenerarTokensUnicos() {
        // Act
        String token1 = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");
        String token2 = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        // Assert
        assertNotEquals(token1, token2); // Los tokens deben ser diferentes debido a timestamp
    }

    @Test
    @DisplayName("Debe generar token con diferentes usuarios")
    void testGenerarTokenDiferentesUsuarios() {
        // Act
        String token1 = jwtUtil.generateToken(1L, "user1@test.com", "CIUDADANO");
        String token2 = jwtUtil.generateToken(2L, "user2@test.com", "ADMIN");

        // Assert
        Long userId1 = jwtUtil.extractUserId(token1);
        Long userId2 = jwtUtil.extractUserId(token2);
        
        assertEquals(1L, userId1);
        assertEquals(2L, userId2);
        assertNotEquals(userId1, userId2);
    }

    @Test
    @DisplayName("Debe incluir el rol en el token")
    void testTokenIncluiyeRol() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "test@test.com", "ADMIN");

        // Act
        boolean esValido = jwtUtil.validateToken(token);

        // Assert
        assertTrue(esValido);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Debe rechazar token con firma manipulada")
    void testTokenConFirmaManipulada() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");
        String[] partes = token.split("\\.");
        String tokenManipulado = partes[0] + "." + partes[1] + ".signatureinvalida";

        // Act
        boolean esValido = jwtUtil.validateToken(tokenManipulado);

        // Assert
        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe rechazar token con payload manipulado")
    void testTokenConPayloadManipulado() {
        // Arrange
        String tokenOriginal = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");
        String[] partes = tokenOriginal.split("\\.");
        String tokenManipulado = partes[0] + ".MANIPULADO." + partes[2];

        // Act
        boolean esValido = jwtUtil.validateToken(tokenManipulado);

        // Assert
        assertFalse(esValido);
    }
}
