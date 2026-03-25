package com.sanosysalvos.ms_mascotas.controller;

import com.sanosysalvos.ms_mascotas.model.*;
import com.sanosysalvos.ms_mascotas.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<?> crearReporte(@RequestBody CrearReporteRequest request) {
        try {
            // Crear mascota primero
            Mascota mascota = new Mascota();
            mascota.setNombre(request.getNombreMascota());
            mascota.setTipo(request.getTipo());
            mascota.setRaza(request.getRaza());
            mascota.setColor(request.getColor());

            // Crear características si existen
            if (request.getCaracteristica() != null) {
                mascota.setCaracteristica(request.getCaracteristica());
            }

            // Crear reporte
            Reporte reporte = reporteService.crearReporte(
                request.getTipoReporte(),
                request.getUsuarioId(),
                mascota,
                request.getUbicacion(),
                request.getLatitud(),
                request.getLongitud(),
                request.getFechaIncidente(),
                request.getDescripcion(),
                request.getTelefonoContacto(),
                request.getEmailContacto()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReporte(@PathVariable Long id) {
        Optional<Reporte> reporte = reporteService.obtenerPorId(id);
        if (reporte.isPresent()) {
            reporteService.incrementarVisualizaciones(id);
            return ResponseEntity.ok(reporte.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte no encontrado");
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> obtenerTodos() {
        return ResponseEntity.ok(reporteService.obtenerTodos());
    }

    @GetMapping("/tipo/perdidos")
    public ResponseEntity<List<Reporte>> obtenerPerdidos() {
        return ResponseEntity.ok(reporteService.obtenerPerdidos());
    }

    @GetMapping("/tipo/encontrados")
    public ResponseEntity<List<Reporte>> obtenerEncontrados() {
        return ResponseEntity.ok(reporteService.obtenerEncontrados());
    }

    @GetMapping("/estado/activos")
    public ResponseEntity<List<Reporte>> obtenerActivos() {
        return ResponseEntity.ok(reporteService.obtenerActivos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reporte>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reporteService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/organizacion/{organizacionId}")
    public ResponseEntity<List<Reporte>> obtenerPorOrganizacion(@PathVariable Long organizacionId) {
        return ResponseEntity.ok(reporteService.obtenerPorOrganizacion(organizacionId));
    }

    @GetMapping("/urgencia")
    public ResponseEntity<List<Reporte>> obtenerUrgentes() {
        return ResponseEntity.ok(reporteService.obtenerPorUrgencia());
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<Reporte>> obtenerPorCaracteristicas(
            @RequestParam String tipo,
            @RequestParam String color,
            @RequestParam String raza) {
        TipoReporte tipoReporte = TipoReporte.valueOf(tipo);
        return ResponseEntity.ok(reporteService.obtenerPorCaracteristicas(tipoReporte, color, raza));
    }

    @GetMapping("/proximidad")
    public ResponseEntity<List<Reporte>> obtenerPorCercaniaGeografica(
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return ResponseEntity.ok(reporteService.obtenerPorCercaniaGeografica(latitud, longitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporte) {
        try {
            Reporte actualizado = reporteService.actualizarReporte(id, reporte);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            EstadoReporte estado = EstadoReporte.valueOf(nuevoEstado);
            reporteService.cambiarEstado(id, estado);
            return ResponseEntity.ok("Estado actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        try {
            reporteService.eliminarReporte(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO para crear reporte
    public static class CrearReporteRequest {
        private TipoReporte tipoReporte;
        private Long usuarioId;
        private String nombreMascota;
        private String tipo;
        private String raza;
        private String color;
        private CaracteristicaMascota caracteristica;
        private String ubicacion;
        private Double latitud;
        private Double longitud;
        private LocalDateTime fechaIncidente;
        private String descripcion;
        private String telefonoContacto;
        private String emailContacto;

        // Getters
        public TipoReporte getTipoReporte() { return tipoReporte; }
        public Long getUsuarioId() { return usuarioId; }
        public String getNombreMascota() { return nombreMascota; }
        public String getTipo() { return tipo; }
        public String getRaza() { return raza; }
        public String getColor() { return color; }
        public CaracteristicaMascota getCaracteristica() { return caracteristica; }
        public String getUbicacion() { return ubicacion; }
        public Double getLatitud() { return latitud; }
        public Double getLongitud() { return longitud; }
        public LocalDateTime getFechaIncidente() { return fechaIncidente; }
        public String getDescripcion() { return descripcion; }
        public String getTelefonoContacto() { return telefonoContacto; }
        public String getEmailContacto() { return emailContacto; }
    }
}
