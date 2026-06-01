package com.sanosysalvos.ms_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.model.Rol;
import com.sanosysalvos.ms_usuarios.security.JwtUtil;
import com.sanosysalvos.ms_usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test de AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("Debe hacer login exitosamente")
    void testLoginExitosamente() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorEmail("test@test.com"))
            .thenReturn(Optional.of(usuario));
        when(usuarioService.validarPassword(usuario, "password123"))
            .thenReturn(true);
        when(jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO"))
            .thenReturn("token123");

        String request = "{\"email\":\"test@test.com\",\"password\":\"password123\"}";

        // Act & Assert
        mockMvc.perform(post("/users/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", equalTo("token123")))
                .andExpect(jsonPath("$.user.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.user.tipoUsuario", equalTo("CIUDADANO")));

        verify(usuarioService, times(1)).obtenerUsuarioPorEmail("test@test.com");
        verify(usuarioService, times(1)).validarPassword(usuario, "password123");
        verify(jwtUtil, times(1)).generateToken(1L, "test@test.com", "CIUDADANO");
    }

    @Test
    @DisplayName("Debe rechazar login con email inexistente")
    void testLoginEmailNoEncontrado() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorEmail("noexiste@test.com"))
            .thenReturn(Optional.empty());

        String request = "{\"email\":\"noexiste@test.com\",\"password\":\"password123\"}";

        // Act & Assert
        mockMvc.perform(post("/users/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo("Credenciales incorrectas")));

        verify(usuarioService, times(1)).obtenerUsuarioPorEmail("noexiste@test.com");
        verify(jwtUtil, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe rechazar login con contraseña incorrecta")
    void testLoginPasswordIncorrecto() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorEmail("test@test.com"))
            .thenReturn(Optional.of(usuario));
        when(usuarioService.validarPassword(usuario, "passwordIncorrecto"))
            .thenReturn(false);

        String request = "{\"email\":\"test@test.com\",\"password\":\"passwordIncorrecto\"}";

        // Act & Assert
        mockMvc.perform(post("/users/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo("Credenciales incorrectas")));

        verify(jwtUtil, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente")
    void testRegisterExitosamente() throws Exception {
        // Arrange
        when(usuarioService.crearUsuario(
            anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(usuario);
        when(jwtUtil.generateToken(1L, "test@test.com", "CIUDADANO"))
            .thenReturn("token123");

        String request = "{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"test@test.com\",\"password\":\"password123\"}";

        // Act & Assert
        mockMvc.perform(post("/users/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", equalTo("token123")))
                .andExpect(jsonPath("$.user.email", equalTo("test@test.com")));

        verify(usuarioService, times(1)).crearUsuario(
            "test@test.com", "test@test.com", "password123", "Test", "User");
    }

    @Test
    @DisplayName("Debe rechazar registro si email ya existe")
    void testRegisterEmailYaExiste() throws Exception {
        // Arrange
        when(usuarioService.crearUsuario(
            anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenThrow(new RuntimeException("El email ya está registrado"));

        String request = "{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"existing@test.com\",\"password\":\"password123\"}";

        // Act & Assert
        mockMvc.perform(post("/users/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("El email ya está registrado")));

        verify(jwtUtil, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe obtener información del usuario autenticado")
    void testObtenerUsuarioAutenticado() throws Exception {
        // Arrange
        String token = "token123";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(1L);
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        // Act & Assert
        mockMvc.perform(get("/users/auth/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.nombre", equalTo("Test")));

        verify(jwtUtil, times(1)).validateToken(token);
        verify(jwtUtil, times(1)).extractUserId(token);
        verify(usuarioService, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    @DisplayName("Debe rechazar si no hay token en header")
    void testObtenerUsuarioSinToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/auth/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo("Token requerido")));

        verify(jwtUtil, never()).validateToken(anyString());
    }

    @Test
    @DisplayName("Debe rechazar si token es inválido")
    void testObtenerUsuarioTokenInvalido() throws Exception {
        // Arrange
        String token = "invalidtoken";
        when(jwtUtil.validateToken(token)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/users/auth/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo("Token inválido")));

        verify(jwtUtil, times(1)).validateToken(token);
        verify(jwtUtil, never()).extractUserId(anyString());
    }

    @Test
    @DisplayName("Debe rechazar si usuario no existe con id del token")
    void testObtenerUsuarioNoEncontrado() throws Exception {
        // Arrange
        String token = "token123";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(99L);
        when(usuarioService.obtenerUsuarioPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/users/auth/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).obtenerUsuarioPorId(99L);
    }

    @Test
    @DisplayName("Debe hacer logout exitosamente")
    void testLogoutExitosamente() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/users/auth/logout")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("Sesión cerrada exitosamente")));
    }
}
