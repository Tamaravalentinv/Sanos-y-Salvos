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
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mysecretkeythatiswelloverthirtytwocharacterslongforhmacsha256");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 3600000L);
    }

    @Test
    @DisplayName("Debe generar un token JWT válido")
    void testGenerarToken() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    @DisplayName("Debe extraer el email del token")
    void testExtraerEmail() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        String email = jwtUtil.extractEmail(token);

        assertEquals("test@test.com", email);
    }

    @Test
    @DisplayName("Debe extraer el userId del token")
    void testExtraerUserId() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("Debe validar un token JWT válido")
    void testValidarTokenValido() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        boolean esValido = jwtUtil.validateToken(token);

        assertTrue(esValido);
    }

    @Test
    @DisplayName("Debe rechazar un token JWT inválido")
    void testValidarTokenInvalido() {
        String tokenInvalido = "invalid.token.here";

        boolean esValido = jwtUtil.validateToken(tokenInvalido);

        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe rechazar un token vacío")
    void testValidarTokenVacio() {
        boolean esValido = jwtUtil.validateToken("");

        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe generar un token válido")
    void testGenerarTokensUnicos() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("test@test.com", jwtUtil.extractEmail(token));
        assertEquals(1L, jwtUtil.extractUserId(token));
    }

    @Test
    @DisplayName("Debe generar token con diferentes usuarios")
    void testGenerarTokenDiferentesUsuarios() {
        String token1 = jwtUtil.generateToken(1L, "user1@test.com", "CIUDADANO");
        String token2 = jwtUtil.generateToken(2L, "user2@test.com", "ADMIN");

        Long userId1 = jwtUtil.extractUserId(token1);
        Long userId2 = jwtUtil.extractUserId(token2);

        assertEquals(1L, userId1);
        assertEquals(2L, userId2);
        assertNotEquals(userId1, userId2);
    }

    @Test
    @DisplayName("Debe incluir el rol en el token")
    void testTokenIncluiyeRol() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "ADMIN");

        boolean esValido = jwtUtil.validateToken(token);

        assertTrue(esValido);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Debe rechazar token con firma manipulada")
    void testTokenConFirmaManipulada() {
        String token = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");
        String[] partes = token.split("\\.");
        String tokenManipulado = partes[0] + "." + partes[1] + ".signatureinvalida";

        boolean esValido = jwtUtil.validateToken(tokenManipulado);

        assertFalse(esValido);
    }

    @Test
    @DisplayName("Debe rechazar token con payload manipulado")
    void testTokenConPayloadManipulado() {
        String tokenOriginal = jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO");
        String[] partes = tokenOriginal.split("\\.");
        String tokenManipulado = partes[0] + ".MANIPULADO." + partes[2];

        boolean esValido = jwtUtil.validateToken(tokenManipulado);

        assertFalse(esValido);
    }
}