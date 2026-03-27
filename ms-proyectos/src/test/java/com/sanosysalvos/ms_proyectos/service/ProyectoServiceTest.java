package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyecto;

    @BeforeEach
    public void setUp() {
        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del proyecto test");
        proyecto.setEstado("ACTIVO");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(3));
        proyecto.setPresupuesto(10000);
        proyecto.setResponsableId(1L);
    }

    @Test
    public void testCrearProyecto() {
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyecto);

        Proyecto nuevoProyecto = proyectoService.crearProyecto(proyecto);

        assertNotNull(nuevoProyecto);
        assertEquals("Proyecto Test", nuevoProyecto.getNombre());
        assertEquals("ACTIVO", nuevoProyecto.getEstado());
        verify(proyectoRepository, times(1)).save(any(Proyecto.class));
    }

    @Test
    public void testObtenerTodosProyectos() {
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(2L);
        proyecto2.setNombre("Proyecto 2");

        when(proyectoRepository.findAll()).thenReturn(Arrays.asList(proyecto, proyecto2));

        List<Proyecto> proyectos = proyectoService.obtenerTodosProyectos();

        assertNotNull(proyectos);
        assertEquals(2, proyectos.size());
        verify(proyectoRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerProyectoPorId() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        Optional<Proyecto> proyectoEncontrado = proyectoService.obtenerProyectoPorId(1L);

        assertTrue(proyectoEncontrado.isPresent());
        assertEquals("Proyecto Test", proyectoEncontrado.get().getNombre());
        verify(proyectoRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerProyectoPorIdNoExistente() {
        when(proyectoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Proyecto> proyectoEncontrado = proyectoService.obtenerProyectoPorId(999L);

        assertFalse(proyectoEncontrado.isPresent());
        verify(proyectoRepository, times(1)).findById(999L);
    }

    @Test
    public void testActualizarProyecto() {
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId(1L);
        proyectoActualizado.setNombre("Proyecto Actualizado");
        proyectoActualizado.setEstado("COMPLETADO");

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyectoActualizado);

        Proyecto resultado = proyectoService.actualizarProyecto(1L, proyectoActualizado);

        assertNotNull(resultado);
        assertEquals("Proyecto Actualizado", resultado.getNombre());
        verify(proyectoRepository, times(1)).findById(1L);
        verify(proyectoRepository, times(1)).save(any(Proyecto.class));
    }

    @Test
    public void testEliminarProyecto() {
        when(proyectoRepository.existsById(1L)).thenReturn(true);

        boolean resultado = proyectoService.eliminarProyecto(1L);

        assertTrue(resultado);
        verify(proyectoRepository, times(1)).existsById(1L);
        verify(proyectoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testObtenerProyectosPorResponsable() {
        when(proyectoRepository.findByResponsableId(1L)).thenReturn(Arrays.asList(proyecto));

        List<Proyecto> proyectos = proyectoService.obtenerProyectosPorResponsable(1L);

        assertNotNull(proyectos);
        assertEquals(1, proyectos.size());
        verify(proyectoRepository, times(1)).findByResponsableId(1L);
    }

    @Test
    public void testObtenerProyectosPorEstado() {
        when(proyectoRepository.findByEstado("ACTIVO")).thenReturn(Arrays.asList(proyecto));

        List<Proyecto> proyectos = proyectoService.obtenerProyectosPorEstado("ACTIVO");

        assertNotNull(proyectos);
        assertEquals(1, proyectos.size());
        assertEquals("ACTIVO", proyectos.get(0).getEstado());
        verify(proyectoRepository, times(1)).findByEstado("ACTIVO");
    }
}
