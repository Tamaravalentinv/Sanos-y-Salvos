package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Usuario;
import com.sanosysalvos.ms_usuarios.model.Rol;
import com.sanosysalvos.ms_usuarios.repository.UsuarioRepository;
import com.sanosysalvos.ms_usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Factory Method Pattern: Crear usuario con rol por defecto
    public Usuario crearUsuario(String username, String email, String password, String nombre, String apellido) {
        // Validaciones
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaCreacion(LocalDateTime.now());
        
        // Asignar rol por defecto (CIUDADANO)
        Rol rolCiudadano = rolRepository.findByNombre("CIUDADANO")
                .orElseGet(() -> {
                    Rol rolNuevo = new Rol("CIUDADANO", "Usuario ciudadano regular");
                    return rolRepository.save(rolNuevo);
                });
        usuario.setRol(rolCiudadano);
        usuario.setEstado("ACTIVO");

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerPorEstado(String estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> obtenerPorOrganizacion(Long organizacionId) {
        return usuarioRepository.findByOrganizacionId(organizacionId);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario u = usuarioExistente.get();
            if (usuario.getNombre() != null) u.setNombre(usuario.getNombre());
            if (usuario.getApellido() != null) u.setApellido(usuario.getApellido());
            if (usuario.getEstado() != null) u.setEstado(usuario.getEstado());
            if (usuario.getContacto() != null) u.setContacto(usuario.getContacto());
            return usuarioRepository.save(u);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void actualizarFechaUltimaConexion(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            u.setFechaUltimaConexion(LocalDateTime.now());
            usuarioRepository.save(u);
        }
    }

    public boolean validarPassword(Usuario usuario, String passwordIngresado) {
        return passwordEncoder.matches(passwordIngresado, usuario.getPassword());
    }
}
