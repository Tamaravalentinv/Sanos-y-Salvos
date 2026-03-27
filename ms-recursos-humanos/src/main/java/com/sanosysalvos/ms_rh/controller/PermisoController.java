package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Permiso;
import com.sanosysalvos.ms_rh.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {
    
    @Autowired
    private PermisoService permisoService;
    
    @PostMapping
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevoPermiso = permisoService.crearPermiso(permiso);
        return new ResponseEntity<>(nuevoPermiso, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Permiso>> obtenerTodosPermisos() {
        List<Permiso> permisos = permisoService.obtenerTodosPermisos();
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Permiso> obtenerPermisoPorId(@PathVariable Long id) {
        Optional<Permiso> permiso = permisoService.obtenerPermisoPorId(id);
        return permiso.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<Permiso>> obtenerPermisosPorEmpleado(@PathVariable Long empleadoId) {
        List<Permiso> permisos = permisoService.obtenerPermisosPorEmpleado(empleadoId);
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Permiso>> obtenerPermisosPorEstado(@PathVariable String estado) {
        List<Permiso> permisos = permisoService.obtenerPermisosPorEstado(estado);
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Permiso> actualizarPermiso(@PathVariable Long id, @RequestBody Permiso permiso) {
        Permiso permisoActualizado = permisoService.actualizarPermiso(id, permiso);
        if (permisoActualizado != null) {
            return new ResponseEntity<>(permisoActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable Long id) {
        if (permisoService.eliminarPermiso(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
