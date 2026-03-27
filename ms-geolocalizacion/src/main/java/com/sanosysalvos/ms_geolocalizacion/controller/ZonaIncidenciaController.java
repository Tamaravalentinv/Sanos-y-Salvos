package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import com.sanosysalvos.ms_geolocalizacion.service.ZonaIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotzones")
@CrossOrigin(origins = "*")
public class ZonaIncidenciaController {

    @Autowired
    private ZonaIncidenciaService zonaService;

    @PostMapping
    public ResponseEntity<ZonaIncidencia> crear(@RequestBody CrearZonaRequest request) {
        ZonaIncidencia zona = zonaService.crearZona(
            request.getNombre(),
            request.getLatitudCentro(),
            request.getLongitudCentro(),
            request.getRadioKm()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(zona);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<ZonaIncidencia> zona = zonaService.obtenerPorId(id);
        return zona.isPresent() 
            ? ResponseEntity.ok(zona.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zona no encontrada");
    }

    @GetMapping
    public ResponseEntity<List<ZonaIncidencia>> obtenerTodas() {
        return ResponseEntity.ok(zonaService.obtenerTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<ZonaIncidencia>> obtenerActivas() {
        return ResponseEntity.ok(zonaService.obtenerActivas());
    }

    @GetMapping("/riesgo/{nivel}")
    public ResponseEntity<List<ZonaIncidencia>> obtenerPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(zonaService.obtenerPorNivelRiesgo(nivel));
    }

    @GetMapping("/alto-riesgo")
    public ResponseEntity<List<ZonaIncidencia>> obtenerAltoRiesgo() {
        return ResponseEntity.ok(zonaService.obtenerZonasAltoRiesgo());
    }

    @GetMapping("/exitosas")
    public ResponseEntity<List<ZonaIncidencia>> obtenerZonasExitosas() {
        return ResponseEntity.ok(zonaService.obtenerZonasExitosas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            zonaService.eliminarZona(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO
    public static class CrearZonaRequest {
        private String nombre;
        private Double latitudCentro;
        private Double longitudCentro;
        private Double radioKm;

        // Getters
        public String getNombre() { return nombre; }
        public Double getLatitudCentro() { return latitudCentro; }
        public Double getLongitudCentro() { return longitudCentro; }
        public Double getRadioKm() { return radioKm; }
    }
}
