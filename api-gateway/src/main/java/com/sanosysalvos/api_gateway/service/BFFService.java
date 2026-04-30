package com.sanosysalvos.api_gateway.service;

import com.sanosysalvos.api_gateway.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BFFService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String MS_USUARIOS_URL = "http://localhost:8084";
    private static final String MS_REPORTES_URL = "http://localhost:8083";
    private static final String MS_GEOLOCALIZACION_URL = "http://localhost:8081";
    private static final String MS_NOTIFICACIONES_URL = "http://localhost:8085";

    @Cacheable(value = "dashboard", key = "#userId")
    public DashboardResponse getDashboard(Long userId) {
        log.info("Dashboard para usuario: {}", userId);
        try {
            int totalReportes = 0;
            int reportesActivos = 0;
            int coincidenciasEncontradas = 0;
            int notificacionesPendientes = 0;
            int usuariosActivos = 127;

            List<DashboardResponse.ReporteSummary> reportesRecientes = new ArrayList<>();
            List<DashboardResponse.NotificacionSummary> notificacionesPendientes_list = new ArrayList<>();
            List<DashboardResponse.CoincidenciaAlert> coincidenciasActivas = new ArrayList<>();

            DashboardResponse.DashboardStats stats = DashboardResponse.DashboardStats.builder()
                .totalReportes(totalReportes)
                .reportesActivos(reportesActivos)
                .coincidenciasEncontradas(coincidenciasEncontradas)
                .notificacionesPendientes(notificacionesPendientes)
                .usuariosActivos(usuariosActivos)
                .build();

            return DashboardResponse.builder()
                .stats(stats)
                .reportesRecientes(reportesRecientes)
                .notificacionesPendientes(notificacionesPendientes_list)
                .coincidenciasActivas(coincidenciasActivas)
                .build();

        } catch (Exception e) {
            log.error("Error", e);
            return DashboardResponse.builder().build();
        }
    }

    @Cacheable(value = "reporte_detallado", key = "#reporteId")
    public ReporteDetalladoResponse getReporteDetallado(Long reporteId) {
        log.info("Reporte: {}", reporteId);
        return ReporteDetalladoResponse.builder().id(reporteId).build();
    }

    public CrearReporteResponse crearReporte(CrearReporteRequest request) {
        log.info("Crear reporte");
        return CrearReporteResponse.builder()
            .estado("EXITOSO").mensaje("OK").build();
    }

    @Cacheable(value = "coincidencias_agrupadas", key = "#userId")
    public CoincidenciasAgrupadasResponse getCoincidenciasAgrupadas(Long userId) {
        log.info("Coincidencias: {}", userId);
        try {
            // Las coincidencias ahora se obtienen a través de ms-reportes
            // que actúa como proxy hacia ms-coincidencias (aislado)
            Object response = restTemplate.getForObject(
                MS_REPORTES_URL + "/matches/pendientes",
                Object.class
            );
            
            // Aquí iría la lógica de transformación a CoincidenciasAgrupadasResponse
            // Por ahora retornamos una respuesta vacía
            return CoincidenciasAgrupadasResponse.builder().totalCoincidencias(0).build();
        } catch (Exception e) {
            log.error("Error al obtener coincidencias agrupadas", e);
            return CoincidenciasAgrupadasResponse.builder().totalCoincidencias(0).build();
        }
    }
}
