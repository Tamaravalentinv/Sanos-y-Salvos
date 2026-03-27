package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @PostMapping
    public ResponseEntity<Proyecto> crearProyecto(@RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.crearProyecto(proyecto);
        return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerTodosProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerTodosProyectos();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyectoPorId(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerProyectoPorId(id);
        return proyecto.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<List<Proyecto>> obtenerProyectosPorResponsable(@PathVariable Long responsableId) {
        List<Proyecto> proyectos = proyectoService.obtenerProyectosPorResponsable(responsableId);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Proyecto>> obtenerProyectosPorEstado(@PathVariable String estado) {
        List<Proyecto> proyectos = proyectoService.obtenerProyectosPorEstado(estado);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizarProyecto(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        Proyecto proyectoActualizado = proyectoService.actualizarProyecto(id, proyecto);
        if (proyectoActualizado != null) {
            return new ResponseEntity<>(proyectoActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        if (proyectoService.eliminarProyecto(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
