package com.sanosysalvos.ms_mascotas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * CoincidenciaProxyController - Delegación transparente a ms-coincidencias
 * 
 * Este controlador actúa como proxy hacia el microservicio de coincidencias,
 * que ahora está aislado del API Gateway. Todas las peticiones de coincidencias
 * se rutean a través de ms-reportes.
 */
@Slf4j
@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "*")
public class CoincidenciaProxyController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String MS_COINCIDENCIAS_URL = "http://ms-coincidencias:8082";

    /**
     * Analizar coincidencias entre dos reportes
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analizarCoincidencia(@RequestBody CoincidenciaAnalisisRequest request) {
        try {
            log.info("Analizando coincidencia - Reporte Perdido: {}, Reporte Encontrado: {}",
                    request.getReportePerdidoId(), request.getReporteEncontradoId());
            
            ResponseEntity<?> response = restTemplate.postForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/analyze",
                    request,
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al analizar coincidencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al analizar coincidencia: " + e.getMessage());
        }
    }

    /**
     * Obtener coincidencia por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            log.info("Obteniendo coincidencia - ID: {}", id);
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/" + id,
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencia", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Coincidencia no encontrada");
        }
    }

    /**
     * Obtener coincidencias de un reporte específico
     */
    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<?> obtenerPorReporte(@PathVariable Long reporteId) {
        try {
            log.info("Obteniendo coincidencias - Reporte ID: {}", reporteId);
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/reporte/" + reporteId,
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencias del reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coincidencias");
        }
    }

    /**
     * Obtener coincidencias pendientes de revisión
     */
    @GetMapping("/pendientes")
    public ResponseEntity<?> obtenerPendientes() {
        try {
            log.info("Obteniendo coincidencias pendientes");
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/pendientes",
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencias pendientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coincidencias pendientes");
        }
    }

    /**
     * Obtener coincidencias confirmadas
     */
    @GetMapping("/confirmadas")
    public ResponseEntity<?> obtenerConfirmadas() {
        try {
            log.info("Obteniendo coincidencias confirmadas");
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/confirmadas",
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencias confirmadas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coincidencias confirmadas");
        }
    }

    /**
     * Obtener coincidencias potenciales por puntaje mínimo
     */
    @GetMapping("/potenciales")
    public ResponseEntity<?> obtenerPotenciales(
            @RequestParam(defaultValue = "70.0") Double puntajeMinimo) {
        try {
            log.info("Obteniendo coincidencias potenciales - Puntaje mínimo: {}", puntajeMinimo);
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/potenciales?puntajeMinimo=" + puntajeMinimo,
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencias potenciales", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coincidencias potenciales");
        }
    }

    /**
     * Obtener coincidencias recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<?> obtenerRecientes(
            @RequestParam(defaultValue = "7") Integer diasAtras) {
        try {
            log.info("Obteniendo coincidencias recientes - Días atrás: {}", diasAtras);
            
            ResponseEntity<?> response = restTemplate.getForEntity(
                    MS_COINCIDENCIAS_URL + "/matches/recientes?diasAtras=" + diasAtras,
                    Object.class
            );
            return response;
        } catch (Exception e) {
            log.error("Error al obtener coincidencias recientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coincidencias recientes");
        }
    }

    /**
     * Confirmar una coincidencia
     */
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCoincidencia(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        try {
            log.info("Confirmando coincidencia - ID: {}, Usuario: {}", id, usuarioId);
            
            return restTemplate.exchange(
                    MS_COINCIDENCIAS_URL + "/matches/" + id + "/confirmar?usuarioId=" + usuarioId,
                    HttpMethod.PATCH,
                    new HttpEntity<>(null),
                    Object.class
            );
        } catch (Exception e) {
            log.error("Error al confirmar coincidencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al confirmar coincidencia");
        }
    }

    /**
     * Rechazar una coincidencia
     */
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarCoincidencia(
            @PathVariable Long id,
            @RequestParam String motivo) {
        try {
            log.info("Rechazando coincidencia - ID: {}, Motivo: {}", id, motivo);
            
            return restTemplate.exchange(
                    MS_COINCIDENCIAS_URL + "/matches/" + id + "/rechazar?motivo=" + motivo,
                    HttpMethod.PATCH,
                    new HttpEntity<>(null),
                    Object.class
            );
        } catch (Exception e) {
            log.error("Error al rechazar coincidencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al rechazar coincidencia");
        }
    }

    /**
     * Resolver un caso de coincidencia
     */
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<?> resolverCaso(@PathVariable Long id) {
        try {
            log.info("Resolviendo caso - ID: {}", id);
            
            return restTemplate.exchange(
                    MS_COINCIDENCIAS_URL + "/matches/" + id + "/resolver",
                    HttpMethod.PATCH,
                    new HttpEntity<>(null),
                    Object.class
            );
        } catch (Exception e) {
            log.error("Error al resolver caso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al resolver caso");
        }
    }

    /**
     * Eliminar una coincidencia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            log.info("Eliminando coincidencia - ID: {}", id);
            
            restTemplate.delete(MS_COINCIDENCIAS_URL + "/matches/" + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar coincidencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar coincidencia");
        }
    }

    /**
     * DTO para solicitud de análisis de coincidencia
     */
    public static class CoincidenciaAnalisisRequest {
        private Long reportePerdidoId;
        private Long reporteEncontradoId;

        public CoincidenciaAnalisisRequest() {}

        public CoincidenciaAnalisisRequest(Long reportePerdidoId, Long reporteEncontradoId) {
            this.reportePerdidoId = reportePerdidoId;
            this.reporteEncontradoId = reporteEncontradoId;
        }

        public Long getReportePerdidoId() { return reportePerdidoId; }
        public void setReportePerdidoId(Long reportePerdidoId) { this.reportePerdidoId = reportePerdidoId; }

        public Long getReporteEncontradoId() { return reporteEncontradoId; }
        public void setReporteEncontradoId(Long reporteEncontradoId) { this.reporteEncontradoId = reporteEncontradoId; }
    }
}
