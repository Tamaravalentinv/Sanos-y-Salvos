package com.sanosysalvos.api_gateway.service;

import com.sanosysalvos.api_gateway.dto.CrearReporteRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BFFServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BFFService service;

    @Test
    void dashboardRetornaEstructuraAgregadaPorDefecto() {
        var dashboard = service.getDashboard(1L);

        assertNotNull(dashboard.getStats());
        assertEquals(127, dashboard.getStats().getUsuariosActivos());
        assertTrue(dashboard.getReportesRecientes().isEmpty());
        assertTrue(dashboard.getNotificacionesPendientes().isEmpty());
        assertTrue(dashboard.getCoincidenciasActivas().isEmpty());
    }

    @Test
    void reporteDetalladoIncluyeIdSolicitado() {
        assertEquals(44L, service.getReporteDetallado(44L).getId());
    }

    @Test
    void crearReporteRetornaRespuestaExitosa() {
        var response = service.crearReporte(new CrearReporteRequest());

        assertEquals("EXITOSO", response.getEstado());
        assertEquals("OK", response.getMensaje());
    }

    @Test
    void coincidenciasAgrupadasConsultaProxyYRetornaVacio() {
        when(restTemplate.getForObject(eq("http://localhost:8083/matches/pendientes"), eq(Object.class)))
                .thenReturn(new Object());

        var response = service.getCoincidenciasAgrupadas(1L);

        assertEquals(0, response.getTotalCoincidencias());
    }

    @Test
    void coincidenciasAgrupadasToleraFallasDelProxy() {
        when(restTemplate.getForObject(eq("http://localhost:8083/matches/pendientes"), eq(Object.class)))
                .thenThrow(new RuntimeException("sin servicio"));

        var response = service.getCoincidenciasAgrupadas(1L);

        assertEquals(0, response.getTotalCoincidencias());
    }
}
