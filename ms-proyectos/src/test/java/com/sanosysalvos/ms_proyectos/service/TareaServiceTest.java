package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @Test
    void crearYConsultarTareasUsaRepositorio() {
        Tarea tarea = tarea("Crear");
        when(tareaRepository.save(tarea)).thenReturn(tarea);
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.findAll()).thenReturn(List.of(tarea));
        when(tareaRepository.findByProyectoId(2L)).thenReturn(List.of(tarea));
        when(tareaRepository.findByAsignadoId(3L)).thenReturn(List.of(tarea));
        when(tareaRepository.findByEstado("PENDIENTE")).thenReturn(List.of(tarea));

        assertSame(tarea, tareaService.crearTarea(tarea));
        assertEquals(Optional.of(tarea), tareaService.obtenerTareaPorId(1L));
        assertEquals(List.of(tarea), tareaService.obtenerTodasTareas());
        assertEquals(List.of(tarea), tareaService.obtenerTareasPorProyecto(2L));
        assertEquals(List.of(tarea), tareaService.obtenerTareasPorAsignado(3L));
        assertEquals(List.of(tarea), tareaService.obtenerTareasPorEstado("PENDIENTE"));
    }

    @Test
    void actualizarTareaExistenteCopiaCamposEditables() {
        Tarea existente = tarea("Antes");
        Tarea cambios = tarea("Despues");
        cambios.setDescripcion("Detalle nuevo");
        cambios.setEstado("COMPLETADA");
        cambios.setPrioridad(3);
        cambios.setFechaVencimiento(LocalDate.now().plusDays(5));
        cambios.setAsignadoId(40L);
        when(tareaRepository.findById(8L)).thenReturn(Optional.of(existente));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarea resultado = tareaService.actualizarTarea(8L, cambios);

        assertEquals("Despues", resultado.getTitulo());
        assertEquals("Detalle nuevo", resultado.getDescripcion());
        assertEquals("COMPLETADA", resultado.getEstado());
        assertEquals(3, resultado.getPrioridad());
        assertEquals(40L, resultado.getAsignadoId());
    }

    @Test
    void actualizarTareaInexistenteNoGuarda() {
        when(tareaRepository.findById(8L)).thenReturn(Optional.empty());

        assertNull(tareaService.actualizarTarea(8L, tarea("Nada")));
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void eliminarTareaValidaExistencia() {
        when(tareaRepository.existsById(1L)).thenReturn(true);
        when(tareaRepository.existsById(2L)).thenReturn(false);

        assertTrue(tareaService.eliminarTarea(1L));
        assertFalse(tareaService.eliminarTarea(2L));
        verify(tareaRepository).deleteById(1L);
    }

    private Tarea tarea(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Descripcion");
        tarea.setProyectoId(2L);
        tarea.setAsignadoId(3L);
        tarea.setEstado("PENDIENTE");
        tarea.setPrioridad(1);
        return tarea;
    }
}
