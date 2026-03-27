package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.repository.TareaRepository;
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
public class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    private Tarea tarea;

    @BeforeEach
    public void setUp() {
        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Tarea Test");
        tarea.setDescripcion("Descripción de la tarea test");
        tarea.setProyectoId(1L);
        tarea.setAsignadoId(1L);
        tarea.setEstado("PENDIENTE");
        tarea.setPrioridad(3);
        tarea.setFechaVencimiento(LocalDate.now().plusDays(7));
    }

    @Test
    public void testCrearTarea() {
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea nuevaTarea = tareaService.crearTarea(tarea);

        assertNotNull(nuevaTarea);
        assertEquals("Tarea Test", nuevaTarea.getTitulo());
        assertEquals("PENDIENTE", nuevaTarea.getEstado());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    public void testObtenerTodasTareas() {
        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");

        when(tareaRepository.findAll()).thenReturn(Arrays.asList(tarea, tarea2));

        List<Tarea> tareas = tareaService.obtenerTodasTareas();

        assertNotNull(tareas);
        assertEquals(2, tareas.size());
        verify(tareaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerTareaPorId() {
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        Optional<Tarea> tareaEncontrada = tareaService.obtenerTareaPorId(1L);

        assertTrue(tareaEncontrada.isPresent());
        assertEquals("Tarea Test", tareaEncontrada.get().getTitulo());
        verify(tareaRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerTareasPorProyecto() {
        when(tareaRepository.findByProyectoId(1L)).thenReturn(Arrays.asList(tarea));

        List<Tarea> tareas = tareaService.obtenerTareasPorProyecto(1L);

        assertNotNull(tareas);
        assertEquals(1, tareas.size());
        assertEquals(1L, tareas.get(0).getProyectoId());
        verify(tareaRepository, times(1)).findByProyectoId(1L);
    }

    @Test
    public void testObtenerTareasPorAsignado() {
        when(tareaRepository.findByAsignadoId(1L)).thenReturn(Arrays.asList(tarea));

        List<Tarea> tareas = tareaService.obtenerTareasPorAsignado(1L);

        assertNotNull(tareas);
        assertEquals(1, tareas.size());
        assertEquals(1L, tareas.get(0).getAsignadoId());
        verify(tareaRepository, times(1)).findByAsignadoId(1L);
    }

    @Test
    public void testObtenerTareasPorEstado() {
        when(tareaRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(tarea));

        List<Tarea> tareas = tareaService.obtenerTareasPorEstado("PENDIENTE");

        assertNotNull(tareas);
        assertEquals(1, tareas.size());
        assertEquals("PENDIENTE", tareas.get(0).getEstado());
        verify(tareaRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    public void testActualizarTarea() {
        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setId(1L);
        tareaActualizada.setTitulo("Tarea Actualizada");
        tareaActualizada.setEstado("COMPLETADA");

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaActualizada);

        Tarea resultado = tareaService.actualizarTarea(1L, tareaActualizada);

        assertNotNull(resultado);
        assertEquals("Tarea Actualizada", resultado.getTitulo());
        verify(tareaRepository, times(1)).findById(1L);
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    public void testEliminarTarea() {
        when(tareaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = tareaService.eliminarTarea(1L);

        assertTrue(resultado);
        verify(tareaRepository, times(1)).existsById(1L);
        verify(tareaRepository, times(1)).deleteById(1L);
    }
}
