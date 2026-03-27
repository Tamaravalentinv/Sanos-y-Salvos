package com.sanosysalvos.ms_proyectos.controller;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    
    @Autowired
    private TareaService tareaService;
    
    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@RequestBody Tarea tarea) {
        Tarea nuevaTarea = tareaService.crearTarea(tarea);
        return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodasTareas() {
        List<Tarea> tareas = tareaService.obtenerTodasTareas();
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerTareaPorId(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaService.obtenerTareaPorId(id);
        return tarea.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorProyecto(@PathVariable Long proyectoId) {
        List<Tarea> tareas = tareaService.obtenerTareasPorProyecto(proyectoId);
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
    
    @GetMapping("/asignado/{asignadoId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorAsignado(@PathVariable Long asignadoId) {
        List<Tarea> tareas = tareaService.obtenerTareasPorAsignado(asignadoId);
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorEstado(@PathVariable String estado) {
        List<Tarea> tareas = tareaService.obtenerTareasPorEstado(estado);
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @RequestBody Tarea tarea) {
        Tarea tareaActualizada = tareaService.actualizarTarea(id, tarea);
        if (tareaActualizada != null) {
            return new ResponseEntity<>(tareaActualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        if (tareaService.eliminarTarea(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
