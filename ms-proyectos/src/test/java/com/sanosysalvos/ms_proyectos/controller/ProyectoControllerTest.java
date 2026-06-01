package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.service.ProyectoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoControllerTest {

    @Mock
    private ProyectoService proyectoService;

    @InjectMocks
    private ProyectoController controller;

    @Test
    void respondeCreatedAlCrearProyecto() {
        Proyecto proyecto = proyecto();
        when(proyectoService.crearProyecto(proyecto)).thenReturn(proyecto);

        ResponseEntity<Proyecto> response = controller.crearProyecto(proyecto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(proyecto, response.getBody());
    }

    @Test
    void respondeOkYNotFoundAlBuscarPorId() {
        Proyecto proyecto = proyecto();
        when(proyectoService.obtenerProyectoPorId(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoService.obtenerProyectoPorId(2L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerProyectoPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerProyectoPorId(2L).getStatusCode());
    }

    @Test
    void listaFiltrosYActualizaProyecto() {
        Proyecto proyecto = proyecto();
        when(proyectoService.obtenerTodosProyectos()).thenReturn(List.of(proyecto));
        when(proyectoService.obtenerProyectosPorResponsable(5L)).thenReturn(List.of(proyecto));
        when(proyectoService.obtenerProyectosPorEstado("ACTIVO")).thenReturn(List.of(proyecto));
        when(proyectoService.actualizarProyecto(1L, proyecto)).thenReturn(proyecto);
        when(proyectoService.actualizarProyecto(2L, proyecto)).thenReturn(null);

        assertEquals(1, controller.obtenerTodosProyectos().getBody().size());
        assertEquals(1, controller.obtenerProyectosPorResponsable(5L).getBody().size());
        assertEquals(1, controller.obtenerProyectosPorEstado("ACTIVO").getBody().size());
        assertEquals(HttpStatus.OK, controller.actualizarProyecto(1L, proyecto).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizarProyecto(2L, proyecto).getStatusCode());
    }

    @Test
    void eliminarProyectoMapeaEstadoSegunResultado() {
        when(proyectoService.eliminarProyecto(1L)).thenReturn(true);
        when(proyectoService.eliminarProyecto(2L)).thenReturn(false);

        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarProyecto(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminarProyecto(2L).getStatusCode());
    }

    private Proyecto proyecto() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setEstado("ACTIVO");
        proyecto.setPresupuesto(100);
        proyecto.setResponsableId(1L);
        return proyecto;
    }
}
