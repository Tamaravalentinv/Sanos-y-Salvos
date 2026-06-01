package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.model.Rol;
import com.sanosysalvos.ms_usuarios.model.Contacto;
import com.sanosysalvos.ms_usuarios.repository.UsuarioRepository;
import com.sanosysalvos.ms_usuarios.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Test de UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Rol rolCiudadano;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializar datos de prueba
        rolCiudadano = new Rol();
        rolCiudadano.setId(1L);
        rolCiudadano.setNombre("CIUDADANO");
        rolCiudadano.setDescripcion("Usuario ciudadano regular");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("encodedPassword");
        usuario.setNombre("Test");
        usuario.setApellido("User");
        usuario.setRol(rolCiudadano);
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear un usuario exitosamente con rol por defecto")
    void testCrearUsuarioExitosamente() {
        // Arrange
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(rolRepository.findByNombre("CIUDADANO")).thenReturn(Optional.of(rolCiudadano));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.crearUsuario("testuser", "test@test.com", "password123", "Test", "User");

        // Assert
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        assertEquals("test@test.com", resultado.getEmail());
        assertEquals("Test", resultado.getNombre());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, times(1)).existsByEmail("test@test.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si username ya existe")
    void testCrearUsuarioConUsernameExistente() {
        // Arrange
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> usuarioService.crearUsuario("testuser", "test@test.com", "password123", "Test", "User"));
        
        assertEquals("El nombre de usuario ya existe", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si email ya existe")
    void testCrearUsuarioConEmailExistente() {
        // Arrange
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> usuarioService.crearUsuario("testuser", "test@test.com", "password123", "Test", "User"));
        
        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener usuario por ID exitosamente")
    void testObtenerUsuarioPorId() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("testuser", resultado.get().getUsername());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si usuario no existe")
    void testObtenerUsuarioPorIdNoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe obtener usuario por username")
    void testObtenerUsuarioPorUsername() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorUsername("testuser");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("test@test.com", resultado.get().getEmail());
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Debe obtener usuario por email")
    void testObtenerUsuarioPorEmail() {
        // Arrange
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorEmail("test@test.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("testuser", resultado.get().getUsername());
        verify(usuarioRepository, times(1)).findByEmail("test@test.com");
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void testObtenerTodos() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("testuser2");
        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener usuarios por estado")
    void testObtenerPorEstado() {
        // Arrange
        List<Usuario> usuariosActivos = Arrays.asList(usuario);
        when(usuarioRepository.findByEstado("ACTIVO")).thenReturn(usuariosActivos);

        // Act
        List<Usuario> resultado = usuarioService.obtenerPorEstado("ACTIVO");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getEstado());
        verify(usuarioRepository, times(1)).findByEstado("ACTIVO");
    }

    @Test
    @DisplayName("Debe obtener usuarios por organización")
    void testObtenerPorOrganizacion() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByOrganizacionId(1L)).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.obtenerPorOrganizacion(1L);

        // Assert
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByOrganizacionId(1L);
    }

    @Test
    @DisplayName("Debe actualizar usuario exitosamente")
    void testActualizarUsuario() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("NuevoNombre");
        usuarioActualizado.setApellido("NuevoApellido");
        
        Usuario usuarioEsperado = usuario;
        usuarioEsperado.setNombre("NuevoNombre");
        usuarioEsperado.setApellido("NuevoApellido");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEsperado);

        // Act
        Usuario resultado = usuarioService.actualizarUsuario(1L, usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("NuevoNombre", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar usuario inexistente")
    void testActualizarUsuarioNoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> usuarioService.actualizarUsuario(99L, usuario));
        
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar usuario exitosamente")
    void testEliminarUsuario() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.eliminarUsuario(1L);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe actualizar fecha de última conexión")
    void testActualizarFechaUltimaConexion() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.actualizarFechaUltimaConexion(1L);

        // Assert
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe validar contraseña correcta")
    void testValidarPasswordCorrecto() {
        // Arrange
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act
        boolean resultado = usuarioService.validarPassword(usuario, "password123");

        // Assert
        assertTrue(resultado);
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
    }

    @Test
    @DisplayName("Debe rechazar contraseña incorrecta")
    void testValidarPasswordIncorrecto() {
        // Arrange
        when(passwordEncoder.matches("passwordIncorrecto", "encodedPassword")).thenReturn(false);

        // Act
        boolean resultado = usuarioService.validarPassword(usuario, "passwordIncorrecto");

        // Assert
        assertFalse(resultado);
        verify(passwordEncoder, times(1)).matches("passwordIncorrecto", "encodedPassword");
    }
}
