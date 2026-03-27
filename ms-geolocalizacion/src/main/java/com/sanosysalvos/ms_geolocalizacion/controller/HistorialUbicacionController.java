package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import com.sanosysalvos.ms_geolocalizacion.service.HistorialUbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historial")
@CrossOrigin(origins = "*")
public class HistorialUbicacionController {

    @Autowired
    private HistorialUbicacionService historialService;

    @PostMapping("/avistamiento")
    public ResponseEntity<HistorialUbicacion> registrarAvistamiento(
            @RequestBody RegistrarAvistamientoRequest request) {
        HistorialUbicacion historial = historialService.registrarAvistamiento(
            request.getReporteId(),
            request.getLatitud(),
            request.getLongitud(),
            request.getDescripcion(),
            request.getQuienReporta(),
            request.getFuenteInformacion(),
            request.getConfiabilidad()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(historial);
    }

    @PostMapping("/hallazgo")
    public ResponseEntity<HistorialUbicacion> registrarHallazgo(
            @RequestBody RegistrarHallazgoRequest request) {
        HistorialUbicacion historial = historialService.registrarHallazgo(
            request.getReporteId(),
            request.getLatitud(),
            request.getLongitud(),
            request.getDescripcion(),
            request.getQuienReporta()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(historial);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<HistorialUbicacion> historial = historialService.obtenerPorId(id);
        return historial.isPresent() 
            ? ResponseEntity.ok(historial.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historial no encontrado");
    }

    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<List<HistorialUbicacion>> obtenerPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(historialService.obtenerHistorialPorReporte(reporteId));
    }

    @GetMapping("/tipo/{tipoEvento}")
    public ResponseEntity<List<HistorialUbicacion>> obtenerPorTipo(@PathVariable String tipoEvento) {
        return ResponseEntity.ok(historialService.obtenerPorTipoEvento(tipoEvento));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialUbicacion>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(historialService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/no-comprobados")
    public ResponseEntity<List<HistorialUbicacion>> obtenerNoComprobados() {
        return ResponseEntity.ok(historialService.obtenerNoComprobados());
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<HistorialUbicacion>> obtenerRecientes(
            @RequestParam(defaultValue = "7") Integer diasAtras) {
        return ResponseEntity.ok(historialService.obtenerRecientes(diasAtras));
    }

    @GetMapping("/confiables")
    public ResponseEntity<List<HistorialUbicacion>> obtenerConfiables(
            @RequestParam(defaultValue = "3") Integer minConfiabilidad) {
        return ResponseEntity.ok(historialService.obtenerPorConfiabilidad(minConfiabilidad));
    }

    @PatchMapping("/{id}/verificar")
    public ResponseEntity<?> verificar(@PathVariable Long id) {
        try {
            historialService.verificar(id);
            return ResponseEntity.ok("Historial verificado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            historialService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTOs
    public static class RegistrarAvistamientoRequest {
        private Long reporteId;
        private Double latitud;
        private Double longitud;
        private String descripcion;
        private Long quienReporta;
        private String fuenteInformacion;
        private Integer confiabilidad;

        // Getters
        public Long getReporteId() { return reporteId; }
        public Double getLatitud() { return latitud; }
        public Double getLongitud() { return longitud; }
        public String getDescripcion() { return descripcion; }
        public Long getQuienReporta() { return quienReporta; }
        public String getFuenteInformacion() { return fuenteInformacion; }
        public Integer getConfiabilidad() { return confiabilidad; }
    }

    public static class RegistrarHallazgoRequest {
        private Long reporteId;
        private Double latitud;
        private Double longitud;
        private String descripcion;
        private Long quienReporta;

        // Getters
        public Long getReporteId() { return reporteId; }
        public Double getLatitud() { return latitud; }
        public Double getLongitud() { return longitud; }
        public String getDescripcion() { return descripcion; }
        public Long getQuienReporta() { return quienReporta; }
    }
}
