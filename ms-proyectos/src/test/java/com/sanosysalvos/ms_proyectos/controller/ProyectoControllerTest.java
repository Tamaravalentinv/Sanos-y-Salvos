package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.service.ProyectoService;
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
public class ProyectoControllerTest {

    @Mock
    private ProyectoService proyectoService;

    @InjectMocks
    private ProyectoController proyectoController;

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
        when(proyectoService.crearProyecto(any(Proyecto.class))).thenReturn(proyecto);

        ResponseEntity<Proyecto> respuesta = proyectoController.crearProyecto(proyecto);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals("Proyecto Test", respuesta.getBody().getNombre());
        verify(proyectoService, times(1)).crearProyecto(any(Proyecto.class));
    }

    @Test
    public void testObtenerTodosProyectos() {
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(2L);
        proyecto2.setNombre("Proyecto 2");

        when(proyectoService.obtenerTodosProyectos()).thenReturn(Arrays.asList(proyecto, proyecto2));

        ResponseEntity<List<Proyecto>> respuesta = proyectoController.obtenerTodosProyectos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(2, respuesta.getBody().size());
        verify(proyectoService, times(1)).obtenerTodosProyectos();
    }

    @Test
    public void testObtenerProyectoPorId() {
        when(proyectoService.obtenerProyectoPorId(1L)).thenReturn(Optional.of(proyecto));

        ResponseEntity<Proyecto> respuesta = proyectoController.obtenerProyectoPorId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Proyecto Test", respuesta.getBody().getNombre());
        verify(proyectoService, times(1)).obtenerProyectoPorId(1L);
    }

    @Test
    public void testObtenerProyectoPorIdNoExistente() {
        when(proyectoService.obtenerProyectoPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Proyecto> respuesta = proyectoController.obtenerProyectoPorId(999L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        verify(proyectoService, times(1)).obtenerProyectoPorId(999L);
    }

    @Test
    public void testActualizarProyecto() {
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setNombre("Proyecto Actualizado");
        proyectoActualizado.setEstado("EN PROGRESO");

        when(proyectoService.actualizarProyecto(eq(1L), any(Proyecto.class)))
                .thenReturn(proyectoActualizado);

        ResponseEntity<Proyecto> respuesta = proyectoController.actualizarProyecto(1L, proyectoActualizado);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Proyecto Actualizado", respuesta.getBody().getNombre());
        verify(proyectoService, times(1)).actualizarProyecto(eq(1L), any(Proyecto.class));
    }

    @Test
    public void testEliminarProyecto() {
        when(proyectoService.eliminarProyecto(1L)).thenReturn(true);

        ResponseEntity<Void> respuesta = proyectoController.eliminarProyecto(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(proyectoService, times(1)).eliminarProyecto(1L);
    }

    @Test
    public void testObtenerProyectosPorResponsable() {
        when(proyectoService.obtenerProyectosPorResponsable(1L))
                .thenReturn(Arrays.asList(proyecto));

        ResponseEntity<List<Proyecto>> respuesta = proyectoController.obtenerProyectosPorResponsable(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(proyectoService, times(1)).obtenerProyectosPorResponsable(1L);
    }

    @Test
    public void testObtenerProyectosPorEstado() {
        when(proyectoService.obtenerProyectosPorEstado("ACTIVO"))
                .thenReturn(Arrays.asList(proyecto));

        ResponseEntity<List<Proyecto>> respuesta = proyectoController.obtenerProyectosPorEstado("ACTIVO");

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(proyectoService, times(1)).obtenerProyectosPorEstado("ACTIVO");
    }
}
