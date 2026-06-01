package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.service.UbicacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UbicacionControllerTest {

    @Mock
    private UbicacionService ubicacionService;

    @InjectMocks
    private UbicacionController controller;

    @Test
    void listarCrearYConsultasDeleganEnServicio() {
        Ubicacion ubicacion = new Ubicacion();
        when(ubicacionService.listar()).thenReturn(List.of(ubicacion));
        when(ubicacionService.guardar(ubicacion)).thenReturn(ubicacion);
        when(ubicacionService.obtenerPorReporte(1L)).thenReturn(List.of(ubicacion));
        when(ubicacionService.obtenerPorComuna("Santiago")).thenReturn(List.of(ubicacion));
        when(ubicacionService.obtenerPorCiudad("Santiago")).thenReturn(List.of(ubicacion));
        when(ubicacionService.obtenerPorCercaniaGeografica(-33.0, -70.0, 5.0)).thenReturn(List.of(ubicacion));
        when(ubicacionService.obtenerRecientes(7)).thenReturn(List.of(ubicacion));

        assertEquals(1, controller.listar().getBody().size());
        assertEquals(HttpStatus.CREATED, controller.crear(ubicacion).getStatusCode());
        assertEquals(1, controller.obtenerPorReporte(1L).getBody().size());
        assertEquals(1, controller.obtenerPorComuna("Santiago").getBody().size());
        assertEquals(1, controller.obtenerPorCiudad("Santiago").getBody().size());
        assertEquals(1, controller.obtenerPorCercania(-33.0, -70.0, 5.0).getBody().size());
        assertEquals(1, controller.obtenerRecientes(7).getBody().size());
    }

    @Test
    void obtenerPorIdRetornaOkONotFound() {
        Ubicacion ubicacion = new Ubicacion();
        when(ubicacionService.obtenerPorId(1L)).thenReturn(Optional.of(ubicacion));
        when(ubicacionService.obtenerPorId(2L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerPorId(2L).getStatusCode());
    }

    @Test
    void actualizarYEliminarManejanExitosYErrores() {
        Ubicacion ubicacion = new Ubicacion();
        doAnswer(invocation -> {
            if (Long.valueOf(2L).equals(invocation.getArgument(0))) {
                throw new IllegalArgumentException("sin id");
            }
            return null;
        }).when(ubicacionService).actualizar(anyLong(), any(Ubicacion.class));
        doAnswer(invocation -> {
            if (Long.valueOf(3L).equals(invocation.getArgument(0))) {
                throw new IllegalStateException("no se puede");
            }
            return null;
        }).when(ubicacionService).eliminar(anyLong());

        assertEquals(HttpStatus.OK, controller.actualizar(1L, ubicacion).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.actualizar(2L, ubicacion).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminar(1L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.eliminar(3L).getStatusCode());

        verify(ubicacionService).actualizar(1L, ubicacion);
        verify(ubicacionService).eliminar(1L);
    }
}
