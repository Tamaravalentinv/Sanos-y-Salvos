package com.sanosysalvos.api_gateway.controller;

import com.sanosysalvos.api_gateway.dto.*;
import com.sanosysalvos.api_gateway.service.BFFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * BFF (Backend For Frontend) Controller
 * Expone endpoints optimizados para el frontend
 * 
 * Ejemplo de rutas:
 * - GET  /api/bff/dashboard
 * - GET  /api/bff/reportes/{id}
 * - POST /api/bff/reportes
 * - GET  /api/bff/coincidencias
 * - GET  /api/bff/notificaciones
 */
@Slf4j
@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class BFFController {

    @Autowired
    private BFFService bffService;

    /**
     * Obtiene el Dashboard completo - Agrega datos de todos los servicios
     * 
     * Respuesta incluye:
     * - Estadísticas generales
     * - Reportes recientes
     * - Notificaciones pendientes
     * - Coincidencias activas
     * 
     * @param userId ID del usuario
     * @return DashboardResponse con datos agregados
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestParam(defaultValue = "1") Long userId) {
        log.info("GET /api/bff/dashboard - userId: {}", userId);
        
        try {
            DashboardResponse dashboard = bffService.getDashboard(userId);
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error en dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un reporte con toda la información relacionada
     * 
     * Agrega:
     * - Datos del reporte
     * - Información de la mascota
     * - Ubicación
     * - Coincidencias encontradas
     * - Historial de cambios
     * - Notificaciones relacionadas
     * 
     * @param reporteId ID del reporte
     * @return ReporteDetalladoResponse
     */
    @GetMapping("/reportes/{reporteId}")
    public ResponseEntity<ReporteDetalladoResponse> getReporteDetallado(
            @PathVariable Long reporteId) {
        log.info("GET /api/bff/reportes/{} - Obteniendo reporte detallado", reporteId);
        
        try {
            ReporteDetalladoResponse reporte = bffService.getReporteDetallado(reporteId);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al obtener reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crea un nuevo reporte desde el frontend
     * 
     * El BFF se encarga de:
     * 1. Crear la mascota en MS Reportes
     * 2. Crear la ubicación en MS Geolocalización
     * 3. Crear el reporte
     * 4. Enviar notificación en MS Notificaciones
     * 
     * Transacción distribuida manejada por el gateway
     * 
     * @param request CrearReporteRequest
     * @return CrearReporteResponse con IDs y estado
     */
    @PostMapping("/reportes")
    public ResponseEntity<CrearReporteResponse> crearReporte(
            @RequestBody CrearReporteRequest request) {
        log.info("POST /api/bff/reportes - Creando nuevo reporte");
        
        try {
            if (request.getNombreMascota() == null || request.getNombreMascota().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            CrearReporteResponse response = bffService.crearReporte(request);
            
            if ("EXITOSO".equals(response.getEstado())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("Error al crear reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene coincidencias agrupadas por reporte
     * 
     * Organiza todas las coincidencias encontradas
     * agrupadas por reporte pérdida
     * 
     * @param userId ID del usuario
     * @return CoincidenciasAgrupadasResponse
     */
    @GetMapping("/coincidencias")
    public ResponseEntity<CoincidenciasAgrupadasResponse> getCoincidenciasAgrupadas(
            @RequestParam(defaultValue = "1") Long userId) {
        log.info("GET /api/bff/coincidencias - userId: {}", userId);
        
        try {
            CoincidenciasAgrupadasResponse coincidencias = bffService.getCoincidenciasAgrupadas(userId);
            return ResponseEntity.ok(coincidencias);
        } catch (Exception e) {
            log.error("Error al obtener coincidencias", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Health check del BFF
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "BFF - Sanos y Salvos"
        ));
    }
}
