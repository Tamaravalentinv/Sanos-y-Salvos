package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import com.sanosysalvos.ms_geolocalizacion.service.HistorialUbicacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialUbicacionControllerTest {

    @Mock
    private HistorialUbicacionService historialService;

    @InjectMocks
    private HistorialUbicacionController controller;

    @Test
    void registrarAvistamientoYHallazgoUsanDtoDelControlador() {
        HistorialUbicacion historial = new HistorialUbicacion();
        HistorialUbicacionController.RegistrarAvistamientoRequest avistamiento =
            new HistorialUbicacionController.RegistrarAvistamientoRequest();
        ReflectionTestUtils.setField(avistamiento, "reporteId", 1L);
        ReflectionTestUtils.setField(avistamiento, "latitud", -33.0);
        ReflectionTestUtils.setField(avistamiento, "longitud", -70.0);
        ReflectionTestUtils.setField(avistamiento, "descripcion", "visto");
        ReflectionTestUtils.setField(avistamiento, "quienReporta", 9L);
        ReflectionTestUtils.setField(avistamiento, "fuenteInformacion", "vecino");
        ReflectionTestUtils.setField(avistamiento, "confiabilidad", 4);

        HistorialUbicacionController.RegistrarHallazgoRequest hallazgo =
            new HistorialUbicacionController.RegistrarHallazgoRequest();
        ReflectionTestUtils.setField(hallazgo, "reporteId", 2L);
        ReflectionTestUtils.setField(hallazgo, "latitud", -34.0);
        ReflectionTestUtils.setField(hallazgo, "longitud", -71.0);
        ReflectionTestUtils.setField(hallazgo, "descripcion", "hallado");
        ReflectionTestUtils.setField(hallazgo, "quienReporta", 10L);

        when(historialService.registrarAvistamiento(1L, -33.0, -70.0, "visto", 9L, "vecino", 4))
            .thenReturn(historial);
        when(historialService.registrarHallazgo(2L, -34.0, -71.0, "hallado", 10L))
            .thenReturn(historial);

        assertEquals(HttpStatus.CREATED, controller.registrarAvistamiento(avistamiento).getStatusCode());
        assertEquals(HttpStatus.CREATED, controller.registrarHallazgo(hallazgo).getStatusCode());
    }

    @Test
    void consultasDeleganEnServicioYObtienenOkONotFound() {
        HistorialUbicacion historial = new HistorialUbicacion();
        when(historialService.obtenerPorId(1L)).thenReturn(Optional.of(historial));
        when(historialService.obtenerPorId(2L)).thenReturn(Optional.empty());
        when(historialService.obtenerHistorialPorReporte(1L)).thenReturn(List.of(historial));
        when(historialService.obtenerPorTipoEvento("AVISTAMIENTO")).thenReturn(List.of(historial));
        when(historialService.obtenerPorUsuario(7L)).thenReturn(List.of(historial));
        when(historialService.obtenerNoComprobados()).thenReturn(List.of(historial));
        when(historialService.obtenerRecientes(5)).thenReturn(List.of(historial));
        when(historialService.obtenerPorConfiabilidad(3)).thenReturn(List.of(historial));

        assertEquals(HttpStatus.OK, controller.obtenerPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerPorId(2L).getStatusCode());
        assertEquals(1, controller.obtenerPorReporte(1L).getBody().size());
        assertEquals(1, controller.obtenerPorTipo("AVISTAMIENTO").getBody().size());
        assertEquals(1, controller.obtenerPorUsuario(7L).getBody().size());
        assertEquals(1, controller.obtenerNoComprobados().getBody().size());
        assertEquals(1, controller.obtenerRecientes(5).getBody().size());
        assertEquals(1, controller.obtenerConfiables(3).getBody().size());
    }

    @Test
    void verificarYEliminarManejanExitosYErrores() {
        doAnswer(invocation -> {
            if (Long.valueOf(2L).equals(invocation.getArgument(0))) {
                throw new IllegalArgumentException("invalido");
            }
            return null;
        }).when(historialService).verificar(anyLong());
        doAnswer(invocation -> {
            if (Long.valueOf(3L).equals(invocation.getArgument(0))) {
                throw new IllegalStateException("ocupado");
            }
            return null;
        }).when(historialService).eliminar(anyLong());

        assertEquals(HttpStatus.OK, controller.verificar(1L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.verificar(2L).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminar(1L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.eliminar(3L).getStatusCode());
    }
}
