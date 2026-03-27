package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Empleado;
import com.sanosysalvos.ms_rh.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerTodosEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodosEmpleados();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleadoPorId(id);
        return empleado.map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorEmail(@PathVariable String email) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleadoPorEmail(email);
        return empleado.map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorCedula(@PathVariable String cedula) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleadoPorCedula(cedula);
        return empleado.map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<Empleado>> obtenerEmpleadosPorDepartamento(@PathVariable Long departamentoId) {
        List<Empleado> empleados = empleadoService.obtenerEmpleadosPorDepartamento(departamentoId);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Empleado>> obtenerEmpleadosPorEstado(@PathVariable String estado) {
        List<Empleado> empleados = empleadoService.obtenerEmpleadosPorEstado(estado);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado empleado) {
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleado);
        if (empleadoActualizado != null) {
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        if (empleadoService.eliminarEmpleado(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
