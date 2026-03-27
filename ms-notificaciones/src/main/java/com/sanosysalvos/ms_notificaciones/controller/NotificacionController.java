package com.sanosysalvos.ms_notificaciones.controller;

import com.sanosysalvos.ms_notificaciones.model.Notificacion;
import com.sanosysalvos.ms_notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<?> crearNotificacion(@RequestBody CrearNotificacionRequest request) {
        try {
            Notificacion notif = notificacionService.crearNotificacion(
                request.getUsuarioId(),
                request.getTipo(),
                request.getAsunto(),
                request.getContenido(),
                request.getDestinatario(),
                request.getEventoTipo(),
                request.getEventoId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(notif);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerNotificacion(@PathVariable Long id) {
        Optional<Notificacion> notif = notificacionService.obtenerPorId(id);
        return notif.isPresent() 
            ? ResponseEntity.ok(notif.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificación no encontrada");
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/user/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerNoLeidasPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(notificacionService.obtenerPorEstado(estado));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(notificacionService.obtenerPorTipo(tipo));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Notificacion>> obtenerPendientes() {
        return ResponseEntity.ok(notificacionService.obtenerPendientes());
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long id) {
        try {
            notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok("Notificación marcada como leída");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long id) {
        try {
            notificacionService.eliminarNotificacion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO para crear notificación
    public static class CrearNotificacionRequest {
        private Long usuarioId;
        private String tipo;
        private String asunto;
        private String contenido;
        private String destinatario;
        private String eventoTipo;
        private Long eventoId;

        // Getters
        public Long getUsuarioId() { return usuarioId; }
        public String getTipo() { return tipo; }
        public String getAsunto() { return asunto; }
        public String getContenido() { return contenido; }
        public String getDestinatario() { return destinatario; }
        public String getEventoTipo() { return eventoTipo; }
        public Long getEventoId() { return eventoId; }
    }
}
