package com.sanosysalvos.ms_coincidencias.controller;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.service.CoincidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "*")
public class CoincidenciaController {

    @Autowired
    private CoincidenciaService coincidenciaService;

    @PostMapping("/analyze")
    public ResponseEntity<Coincidencia> analizarCoincidencia(
            @RequestBody AnalizarCoincidenciaRequest request) {
        Coincidencia coincidencia = coincidenciaService.crearCoincidencia(
            request.getReportePerdidoId(),
            request.getReporteEncontradoId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(coincidencia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Coincidencia> coincidencia = coincidenciaService.obtenerPorId(id);
        return coincidencia.isPresent() 
            ? ResponseEntity.ok(coincidencia.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coincidencia no encontrada");
    }

    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<List<Coincidencia>> obtenerPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(coincidenciaService.obtenerPorReporte(reporteId));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Coincidencia>> obtenerPendientes() {
        return ResponseEntity.ok(coincidenciaService.obtenerPendientes());
    }

    @GetMapping("/confirmadas")
    public ResponseEntity<List<Coincidencia>> obtenerConfirmadas() {
        return ResponseEntity.ok(coincidenciaService.obtenerConfirmadas());
    }

    @GetMapping("/potenciales")
    public ResponseEntity<List<Coincidencia>> obtenerPotenciales(
            @RequestParam(defaultValue = "70.0") Double puntajeMinimo) {
        return ResponseEntity.ok(coincidenciaService.obtenerPotenciales(puntajeMinimo));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<Coincidencia>> obtenerRecientes(
            @RequestParam(defaultValue = "7") Integer diasAtras) {
        return ResponseEntity.ok(coincidenciaService.obtenerRecientes(diasAtras));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCoincidencia(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        try {
            Coincidencia coincidencia = coincidenciaService.confirmarCoincidencia(id, usuarioId);
            return ResponseEntity.ok(coincidencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarCoincidencia(
            @PathVariable Long id,
            @RequestParam String motivo) {
        try {
            Coincidencia coincidencia = coincidenciaService.rechazarCoincidencia(id, motivo);
            return ResponseEntity.ok(coincidencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/resolver")
    public ResponseEntity<?> resolverCaso(@PathVariable Long id) {
        try {
            Coincidencia coincidencia = coincidenciaService.resolverCaso(id);
            return ResponseEntity.ok(coincidencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            coincidenciaService.eliminarCoincidencia(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO
    public static class AnalizarCoincidenciaRequest {
        private Long reportePerdidoId;
        private Long reporteEncontradoId;

        // Getters
        public Long getReportePerdidoId() { return reportePerdidoId; }
        public Long getReporteEncontradoId() { return reporteEncontradoId; }
    }
}