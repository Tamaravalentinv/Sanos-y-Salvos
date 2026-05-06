package com.sanosysalvos.ms_usuarios.controller;

import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.security.JwtUtil;
import com.sanosysalvos.ms_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorEmail(request.getEmail());
        if (usuarioOpt.isEmpty() || !usuarioService.validarPassword(usuarioOpt.get(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales incorrectas"));
        }
        Usuario usuario = usuarioOpt.get();
        String token = jwtUtil.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().getNombre());
        return ResponseEntity.ok(new AuthResponse(token, toUserDTO(usuario)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = usuarioService.crearUsuario(
                    request.getEmail(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getNombre(),
                    request.getApellido()
            );
            String token = jwtUtil.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, toUserDTO(usuario)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token requerido"));
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token inválido"));
        }
        Long userId = jwtUtil.extractUserId(token);
        return usuarioService.obtenerUsuarioPorId(userId)
                .map(u -> ResponseEntity.ok(toUserDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada exitosamente"));
    }

    private UserDTO toUserDTO(Usuario u) {
        String telefono = (u.getContacto() != null) ? u.getContacto().getTelefono() : null;
        String tipoUsuario = (u.getRol() != null) ? u.getRol().getNombre() : "CIUDADANO";
        return new UserDTO(
                String.valueOf(u.getId()),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                tipoUsuario,
                telefono,
                null
        );
    }

    // ─── DTOs ───────────────────────────────────────────────────────────────────

    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static class RegisterRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String password;
        private String tipoUsuario;
        private String telefono;
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getTipoUsuario() { return tipoUsuario; }
        public String getTelefono() { return telefono; }
    }

    public static class UserDTO {
        public final String id;
        public final String nombre;
        public final String apellido;
        public final String email;
        public final String tipoUsuario;
        public final String telefono;
        public final String fotoPerfil;

        public UserDTO(String id, String nombre, String apellido, String email,
                       String tipoUsuario, String telefono, String fotoPerfil) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.tipoUsuario = tipoUsuario;
            this.telefono = telefono;
            this.fotoPerfil = fotoPerfil;
        }
    }

    public static class AuthResponse {
        public final String token;
        public final UserDTO user;
        public AuthResponse(String token, UserDTO user) {
            this.token = token;
            this.user = user;
        }
    }
}
