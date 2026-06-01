package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import com.sanosysalvos.ms_geolocalizacion.service.ZonaIncidenciaService;
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
class ZonaIncidenciaControllerTest {

    @Mock
    private ZonaIncidenciaService zonaService;

    @InjectMocks
    private ZonaIncidenciaController controller;

    @Test
    void crearUsaRequestYConsultasDeleganEnServicio() {
        ZonaIncidencia zona = new ZonaIncidencia();
        ZonaIncidenciaController.CrearZonaRequest request = new ZonaIncidenciaController.CrearZonaRequest();
        ReflectionTestUtils.setField(request, "nombre", "Centro");
        ReflectionTestUtils.setField(request, "latitudCentro", -33.0);
        ReflectionTestUtils.setField(request, "longitudCentro", -70.0);
        ReflectionTestUtils.setField(request, "radioKm", 3.5);

        when(zonaService.crearZona("Centro", -33.0, -70.0, 3.5)).thenReturn(zona);
        when(zonaService.obtenerTodas()).thenReturn(List.of(zona));
        when(zonaService.obtenerActivas()).thenReturn(List.of(zona));
        when(zonaService.obtenerPorNivelRiesgo("ALTO")).thenReturn(List.of(zona));
        when(zonaService.obtenerZonasAltoRiesgo()).thenReturn(List.of(zona));
        when(zonaService.obtenerZonasExitosas()).thenReturn(List.of(zona));

        assertEquals(HttpStatus.CREATED, controller.crear(request).getStatusCode());
        assertEquals(1, controller.obtenerTodas().getBody().size());
        assertEquals(1, controller.obtenerActivas().getBody().size());
        assertEquals(1, controller.obtenerPorNivel("ALTO").getBody().size());
        assertEquals(1, controller.obtenerAltoRiesgo().getBody().size());
        assertEquals(1, controller.obtenerZonasExitosas().getBody().size());
    }

    @Test
    void obtenerPorIdRetornaOkONotFound() {
        ZonaIncidencia zona = new ZonaIncidencia();
        when(zonaService.obtenerPorId(1L)).thenReturn(Optional.of(zona));
        when(zonaService.obtenerPorId(2L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerPorId(2L).getStatusCode());
    }

    @Test
    void eliminarManejaExitoYError() {
        doAnswer(invocation -> {
            if (Long.valueOf(2L).equals(invocation.getArgument(0))) {
                throw new IllegalArgumentException("no existe");
            }
            return null;
        }).when(zonaService).eliminarZona(anyLong());

        assertEquals(HttpStatus.NO_CONTENT, controller.eliminar(1L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.eliminar(2L).getStatusCode());

        verify(zonaService).eliminarZona(1L);
        verify(zonaService).eliminarZona(2L);
    }
}
