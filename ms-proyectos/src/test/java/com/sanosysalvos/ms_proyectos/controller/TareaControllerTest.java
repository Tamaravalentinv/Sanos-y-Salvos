package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.service.TareaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController controller;

    @Test
    void crearTareaRespondeCreated() {
        Tarea tarea = tarea();
        when(tareaService.crearTarea(tarea)).thenReturn(tarea);

        var response = controller.crearTarea(tarea);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(tarea, response.getBody());
    }

    @Test
    void rutasDeConsultaDevuelvenResultados() {
        Tarea tarea = tarea();
        when(tareaService.obtenerTodasTareas()).thenReturn(List.of(tarea));
        when(tareaService.obtenerTareasPorProyecto(1L)).thenReturn(List.of(tarea));
        when(tareaService.obtenerTareasPorAsignado(2L)).thenReturn(List.of(tarea));
        when(tareaService.obtenerTareasPorEstado("PENDIENTE")).thenReturn(List.of(tarea));

        assertEquals(1, controller.obtenerTodasTareas().getBody().size());
        assertEquals(1, controller.obtenerTareasPorProyecto(1L).getBody().size());
        assertEquals(1, controller.obtenerTareasPorAsignado(2L).getBody().size());
        assertEquals(1, controller.obtenerTareasPorEstado("PENDIENTE").getBody().size());
    }

    @Test
    void obtenerYActualizarManejanEncontradoEInexistente() {
        Tarea tarea = tarea();
        when(tareaService.obtenerTareaPorId(1L)).thenReturn(Optional.of(tarea));
        when(tareaService.obtenerTareaPorId(2L)).thenReturn(Optional.empty());
        when(tareaService.actualizarTarea(1L, tarea)).thenReturn(tarea);
        when(tareaService.actualizarTarea(2L, tarea)).thenReturn(null);

        assertEquals(HttpStatus.OK, controller.obtenerTareaPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerTareaPorId(2L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.actualizarTarea(1L, tarea).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizarTarea(2L, tarea).getStatusCode());
    }

    @Test
    void eliminarTareaMapeaNoContentONotFound() {
        when(tareaService.eliminarTarea(1L)).thenReturn(true);
        when(tareaService.eliminarTarea(2L)).thenReturn(false);

        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarTarea(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminarTarea(2L).getStatusCode());
    }

    private Tarea tarea() {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Tarea");
        tarea.setProyectoId(1L);
        tarea.setAsignadoId(2L);
        tarea.setEstado("PENDIENTE");
        tarea.setPrioridad(1);
        return tarea;
    }
}
