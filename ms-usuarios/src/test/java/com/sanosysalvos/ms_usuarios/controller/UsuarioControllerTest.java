package com.sanosysalvos.ms_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.model.Rol;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test de UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

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
        usuario.setNombre("Test");
        usuario.setApellido("User");
        usuario.setRol(rol);
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente")
    void testRegistrarUsuarioExitosamente() throws Exception {
        // Arrange
        when(usuarioService.crearUsuario(
            anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(usuario);

        String request = "{\"username\":\"testuser\",\"email\":\"test@test.com\",\"password\":\"password123\",\"nombre\":\"Test\",\"apellido\":\"User\"}";

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", equalTo("testuser")))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")));

        verify(usuarioService, times(1)).crearUsuario(
            "testuser", "test@test.com", "password123", "Test", "User");
    }

    @Test
    @DisplayName("Debe retornar error 400 si username ya existe")
    void testRegistrarUsuarioConUsernameExistente() throws Exception {
        // Arrange
        when(usuarioService.crearUsuario(
            anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenThrow(new RuntimeException("El nombre de usuario ya existe"));

        String request = "{\"username\":\"existinguser\",\"email\":\"new@test.com\",\"password\":\"password123\",\"nombre\":\"Test\",\"apellido\":\"User\"}";

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("El nombre de usuario ya existe")));
    }

    @Test
    @DisplayName("Debe obtener usuario por ID exitosamente")
    void testObtenerUsuarioPorId() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        // Act & Assert
        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo("testuser")))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")));

        verify(usuarioService, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 si usuario no existe")
    void testObtenerUsuarioPorIdNoEncontrado() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/users/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuario no encontrado")));
    }

    @Test
    @DisplayName("Debe obtener usuario por username")
    void testObtenerPorUsername() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuarioPorUsername("testuser")).thenReturn(Optional.of(usuario));

        // Act & Assert
        mockMvc.perform(get("/users/username/testuser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("testuser")));

        verify(usuarioService, times(1)).obtenerUsuarioPorUsername("testuser");
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void testObtenerTodos() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", equalTo("testuser")));

        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("Debe obtener usuarios por estado")
    void testObtenerPorEstado() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerPorEstado("ACTIVO")).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/users/estado/ACTIVO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", equalTo("ACTIVO")));

        verify(usuarioService, times(1)).obtenerPorEstado("ACTIVO");
    }

    @Test
    @DisplayName("Debe obtener usuarios por organización")
    void testObtenerPorOrganizacion() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerPorOrganizacion(1L)).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/users/organizacion/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(usuarioService, times(1)).obtenerPorOrganizacion(1L);
    }

    @Test
    @DisplayName("Debe actualizar usuario exitosamente")
    void testActualizarUsuario() throws Exception {
        // Arrange
        Usuario usuarioActualizado = usuario;
        usuarioActualizado.setNombre("Nuevo Nombre");
        
        when(usuarioService.actualizarUsuario(eq(1L), org.mockito.ArgumentMatchers.any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act & Assert
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", equalTo("Nuevo Nombre")));

        verify(usuarioService, times(1)).actualizarUsuario(eq(1L), org.mockito.ArgumentMatchers.any(Usuario.class));
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar usuario inexistente")
    void testActualizarUsuarioNoEncontrado() throws Exception {
        // Arrange
        when(usuarioService.actualizarUsuario(eq(99L), org.mockito.ArgumentMatchers.any(Usuario.class)))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/users/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuario no encontrado")));
    }

    @Test
    @DisplayName("Debe eliminar usuario exitosamente")
    void testEliminarUsuario() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminarUsuario(1L);

        // Act & Assert
        mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }

    @Test
    @DisplayName("Debe actualizar fecha de último acceso")
    void testActualizarUltimoAcceso() throws Exception {
        // Arrange
        doNothing().when(usuarioService).actualizarFechaUltimaConexion(1L);

        // Act & Assert
        mockMvc.perform(post("/users/1/ultimo-acceso")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Fecha de acceso actualizada")));

        verify(usuarioService, times(1)).actualizarFechaUltimaConexion(1L);
    }

    @Test
    @DisplayName("Debe retornar lista vacía de usuarios")
    void testObtenerTodosVacio() throws Exception {
        // Arrange
        when(usuarioService.obtenerTodos()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
