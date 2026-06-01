package com.sanosysalvos.api_gateway.controller;

import com.sanosysalvos.api_gateway.dto.*;
import com.sanosysalvos.api_gateway.service.BFFService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BFFControllerTest {

    @Mock
    private BFFService service;

    @InjectMocks
    private BFFController controller;

    @Test
    void dashboardReporteYCoincidenciasRespondenOk() {
        when(service.getDashboard(1L)).thenReturn(DashboardResponse.builder().build());
        when(service.getReporteDetallado(2L)).thenReturn(ReporteDetalladoResponse.builder().id(2L).build());
        when(service.getCoincidenciasAgrupadas(3L)).thenReturn(CoincidenciasAgrupadasResponse.builder().totalCoincidencias(0).build());

        assertEquals(HttpStatus.OK, controller.getDashboard(1L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getReporteDetallado(2L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getCoincidenciasAgrupadas(3L).getStatusCode());
    }

    @Test
    void crearReporteValidaNombreYMapeaEstadoDeServicio() {
        CrearReporteRequest invalido = new CrearReporteRequest();
        CrearReporteRequest valido = new CrearReporteRequest();
        valido.setNombreMascota("Luna");
        when(service.crearReporte(valido)).thenReturn(CrearReporteResponse.builder().estado("EXITOSO").mensaje("OK").build());

        assertEquals(HttpStatus.BAD_REQUEST, controller.crearReporte(invalido).getStatusCode());
        assertEquals(HttpStatus.CREATED, controller.crearReporte(valido).getStatusCode());
    }

    @Test
    void crearReporteRetornaBadRequestCuandoServicioNoEsExitoso() {
        CrearReporteRequest request = new CrearReporteRequest();
        request.setNombreMascota("Luna");
        when(service.crearReporte(request)).thenReturn(CrearReporteResponse.builder().estado("ERROR").mensaje("Datos invalidos").build());

        assertEquals(HttpStatus.BAD_REQUEST, controller.crearReporte(request).getStatusCode());
    }

    @Test
    void erroresDelServicioRetornanInternalServerError() {
        CrearReporteRequest request = new CrearReporteRequest();
        request.setNombreMascota("Luna");
        when(service.getDashboard(1L)).thenThrow(new RuntimeException("error"));
        when(service.getReporteDetallado(2L)).thenThrow(new RuntimeException("error"));
        when(service.crearReporte(request)).thenThrow(new RuntimeException("error"));
        when(service.getCoincidenciasAgrupadas(3L)).thenThrow(new RuntimeException("error"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.getDashboard(1L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.getReporteDetallado(2L).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.crearReporte(request).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.getCoincidenciasAgrupadas(3L).getStatusCode());
    }

    @Test
    void healthExponeEstadoDelBff() {
        var response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
    }
}
