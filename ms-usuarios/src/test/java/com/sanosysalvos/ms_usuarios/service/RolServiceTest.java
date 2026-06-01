package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Rol;
import com.sanosysalvos.ms_usuarios.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Test de RolService")
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("CIUDADANO");
        rol.setDescripcion("Usuario ciudadano regular");
    }

    @Test
    @DisplayName("Debe crear un rol exitosamente")
    void testCrearRolExitosamente() {
        // Arrange
        when(rolRepository.findByNombre("CIUDADANO")).thenReturn(Optional.empty());
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        // Act
        Rol resultado = rolService.crearRol("CIUDADANO", "Usuario ciudadano regular");

        // Assert
        assertNotNull(resultado);
        assertEquals("CIUDADANO", resultado.getNombre());
        verify(rolRepository, times(1)).findByNombre("CIUDADANO");
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol ya existe")
    void testCrearRolYaExistente() {
        // Arrange
        when(rolRepository.findByNombre("CIUDADANO")).thenReturn(Optional.of(rol));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> rolService.crearRol("CIUDADANO", "Usuario ciudadano regular"));
        
        assertEquals("El rol ya existe", exception.getMessage());
        verify(rolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener rol por ID exitosamente")
    void testObtenerPorId() {
        // Arrange
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        // Act
        Optional<Rol> resultado = rolService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("CIUDADANO", resultado.get().getNombre());
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si rol no existe")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Rol> resultado = rolService.obtenerPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe obtener rol por nombre")
    void testObtenerPorNombre() {
        // Arrange
        when(rolRepository.findByNombre("CIUDADANO")).thenReturn(Optional.of(rol));

        // Act
        Optional<Rol> resultado = rolService.obtenerPorNombre("CIUDADANO");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("CIUDADANO", resultado.get().getNombre());
        verify(rolRepository, times(1)).findByNombre("CIUDADANO");
    }

    @Test
    @DisplayName("Debe obtener todos los roles")
    void testObtenerTodos() {
        // Arrange
        Rol rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("ADMIN");
        List<Rol> roles = Arrays.asList(rol, rol2);
        when(rolRepository.findAll()).thenReturn(roles);

        // Act
        List<Rol> resultado = rolService.obtenerTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe eliminar un rol exitosamente")
    void testEliminarRol() {
        // Arrange
        doNothing().when(rolRepository).deleteById(1L);

        // Act
        rolService.eliminarRol(1L);

        // Assert
        verify(rolRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe obtener lista vacía cuando no hay roles")
    void testObtenerTodosVacio() {
        // Arrange
        when(rolRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Rol> resultado = rolService.obtenerTodos();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(rolRepository, times(1)).findAll();
    }
}
