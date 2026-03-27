package com.sanosysalvos.ms_usuarios.controller;

import com.sanosysalvos.ms_usuarios.model.Organizacion;
import com.sanosysalvos.ms_usuarios.service.OrganizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
@CrossOrigin(origins = "*")
public class OrganizacionController {

    @Autowired
    private OrganizacionService organizacionService;

    @PostMapping
    public ResponseEntity<?> crearOrganizacion(@RequestBody CrearOrganizacionRequest request) {
        try {
            Organizacion org = organizacionService.crearOrganizacion(
                request.getNombre(),
                request.getTipoId(),
                request.getDescripcion()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(org);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrganizacion(@PathVariable Long id) {
        Optional<Organizacion> org = organizacionService.obtenerPorId(id);
        return org.isPresent() 
            ? ResponseEntity.ok(org.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organización no encontrada");
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        Optional<Organizacion> org = organizacionService.obtenerPorNombre(nombre);
        return org.isPresent() 
            ? ResponseEntity.ok(org.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organización no encontrada");
    }

    @GetMapping
    public ResponseEntity<List<Organizacion>> obtenerTodas() {
        return ResponseEntity.ok(organizacionService.obtenerTodas());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Organizacion>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(organizacionService.obtenerPorEstado(estado));
    }

    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<Organizacion>> obtenerPorTipo(@PathVariable Long tipoId) {
        return ResponseEntity.ok(organizacionService.obtenerPorTipo(tipoId));
    }

    @GetMapping("/verificadas")
    public ResponseEntity<List<Organizacion>> obtenerVerificadas() {
        return ResponseEntity.ok(organizacionService.obtenerVerificadas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrganizacion(@PathVariable Long id, @RequestBody Organizacion org) {
        try {
            Organizacion actualizada = organizacionService.actualizarOrganizacion(id, org);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/verificar")
    public ResponseEntity<?> verificarOrganizacion(@PathVariable Long id) {
        try {
            organizacionService.verificarOrganizacion(id);
            return ResponseEntity.ok("Organización verificada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrganizacion(@PathVariable Long id) {
        try {
            organizacionService.eliminarOrganizacion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO para crear organización
    public static class CrearOrganizacionRequest {
        private String nombre;
        private Long tipoId;
        private String descripcion;

        // Getters
        public String getNombre() { return nombre; }
        public Long getTipoId() { return tipoId; }
        public String getDescripcion() { return descripcion; }
    }
}
