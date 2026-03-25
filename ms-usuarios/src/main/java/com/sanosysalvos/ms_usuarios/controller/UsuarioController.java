package com.sanosysalvos.ms_usuarios.controller;

import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = usuarioService.crearUsuario(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getNombre(),
                request.getApellido()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.isPresent() 
            ? ResponseEntity.ok(usuario.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> obtenerPorUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return usuario.isPresent() 
            ? ResponseEntity.ok(usuario.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(usuarioService.obtenerPorEstado(estado));
    }

    @GetMapping("/organizacion/{organizacionId}")
    public ResponseEntity<List<Usuario>> obtenerPorOrganizacion(@PathVariable Long organizacionId) {
        return ResponseEntity.ok(usuarioService.obtenerPorOrganizacion(organizacionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/ultimo-acceso")
    public ResponseEntity<?> actualizarUltimoAcceso(@PathVariable Long id) {
        try {
            usuarioService.actualizarFechaUltimaConexion(id);
            return ResponseEntity.ok("Fecha de acceso actualizada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO para registro
    public static class RegistroRequest {
        private String username;
        private String email;
        private String password;
        private String nombre;
        private String apellido;

        // Getters
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
    }
}
