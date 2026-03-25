package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/map")
@CrossOrigin(origins = "*")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<List<Ubicacion>> listar() {
        return ResponseEntity.ok(ubicacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Ubicacion> ubicacion = ubicacionService.obtenerPorId(id);
        return ubicacion.isPresent() 
            ? ResponseEntity.ok(ubicacion.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ubicación no encontrada");
    }

    @PostMapping
    public ResponseEntity<Ubicacion> crear(@RequestBody Ubicacion ubicacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionService.guardar(ubicacion));
    }

    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<List<Ubicacion>> obtenerPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(ubicacionService.obtenerPorReporte(reporteId));
    }

    @GetMapping("/comuna/{comuna}")
    public ResponseEntity<List<Ubicacion>> obtenerPorComuna(@PathVariable String comuna) {
        return ResponseEntity.ok(ubicacionService.obtenerPorComuna(comuna));
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Ubicacion>> obtenerPorCiudad(@PathVariable String ciudad) {
        return ResponseEntity.ok(ubicacionService.obtenerPorCiudad(ciudad));
    }

    @GetMapping("/cercania")
    public ResponseEntity<List<Ubicacion>> obtenerPorCercania(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5") Double radioKm) {
        return ResponseEntity.ok(ubicacionService.obtenerPorCercaniaGeografica(latitud, longitud, radioKm));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<Ubicacion>> obtenerRecientes(
            @RequestParam(defaultValue = "7") Integer diasAtras) {
        return ResponseEntity.ok(ubicacionService.obtenerRecientes(diasAtras));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Ubicacion ubicacion) {
        try {
            ubicacionService.actualizar(id, ubicacion);
            return ResponseEntity.ok("Ubicación actualizada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            ubicacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
