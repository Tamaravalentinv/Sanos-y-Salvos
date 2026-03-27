package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

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
        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tarea);

        ResponseEntity<Tarea> respuesta = tareaController.crearTarea(tarea);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals("Tarea Test", respuesta.getBody().getTitulo());
        verify(tareaService, times(1)).crearTarea(any(Tarea.class));
    }

    @Test
    public void testObtenerTodasTareas() {
        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");

        when(tareaService.obtenerTodasTareas()).thenReturn(Arrays.asList(tarea, tarea2));

        ResponseEntity<List<Tarea>> respuesta = tareaController.obtenerTodasTareas();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(2, respuesta.getBody().size());
        verify(tareaService, times(1)).obtenerTodasTareas();
    }

    @Test
    public void testObtenerTareaPorId() {
        when(tareaService.obtenerTareaPorId(1L)).thenReturn(Optional.of(tarea));

        ResponseEntity<Tarea> respuesta = tareaController.obtenerTareaPorId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Tarea Test", respuesta.getBody().getTitulo());
        verify(tareaService, times(1)).obtenerTareaPorId(1L);
    }

    @Test
    public void testObtenerTareaPorIdNoExistente() {
        when(tareaService.obtenerTareaPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Tarea> respuesta = tareaController.obtenerTareaPorId(999L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        verify(tareaService, times(1)).obtenerTareaPorId(999L);
    }

    @Test
    public void testActualizarTarea() {
        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setId(1L);
        tareaActualizada.setTitulo("Tarea Actualizada");
        tareaActualizada.setEstado("COMPLETADA");

        when(tareaService.actualizarTarea(eq(1L), any(Tarea.class)))
                .thenReturn(tareaActualizada);

        ResponseEntity<Tarea> respuesta = tareaController.actualizarTarea(1L, tareaActualizada);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Tarea Actualizada", respuesta.getBody().getTitulo());
        verify(tareaService, times(1)).actualizarTarea(eq(1L), any(Tarea.class));
    }

    @Test
    public void testEliminarTarea() {
        when(tareaService.eliminarTarea(1L)).thenReturn(true);

        ResponseEntity<Void> respuesta = tareaController.eliminarTarea(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(tareaService, times(1)).eliminarTarea(1L);
    }

    @Test
    public void testObtenerTareasPorProyecto() {
        when(tareaService.obtenerTareasPorProyecto(1L))
                .thenReturn(Arrays.asList(tarea));

        ResponseEntity<List<Tarea>> respuesta = tareaController.obtenerTareasPorProyecto(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(tareaService, times(1)).obtenerTareasPorProyecto(1L);
    }

    @Test
    public void testObtenerTareasPorEstado() {
        when(tareaService.obtenerTareasPorEstado("PENDIENTE"))
                .thenReturn(Arrays.asList(tarea));

        ResponseEntity<List<Tarea>> respuesta = tareaController.obtenerTareasPorEstado("PENDIENTE");

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(tareaService, times(1)).obtenerTareasPorEstado("PENDIENTE");
    }
}
