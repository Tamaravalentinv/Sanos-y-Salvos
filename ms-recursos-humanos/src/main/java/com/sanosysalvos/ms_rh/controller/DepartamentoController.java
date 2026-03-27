package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Departamento;
import com.sanosysalvos.ms_rh.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {
    
    @Autowired
    private DepartamentoService departamentoService;
    
    @PostMapping
    public ResponseEntity<Departamento> crearDepartamento(@RequestBody Departamento departamento) {
        Departamento nuevoDepartamento = departamentoService.crearDepartamento(departamento);
        return new ResponseEntity<>(nuevoDepartamento, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Departamento>> obtenerTodosDepartamentos() {
        List<Departamento> departamentos = departamentoService.obtenerTodosDepartamentos();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> obtenerDepartamentoPorId(@PathVariable Long id) {
        Optional<Departamento> departamento = departamentoService.obtenerDepartamentoPorId(id);
        return departamento.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Departamento> obtenerDepartamentoPorNombre(@PathVariable String nombre) {
        Optional<Departamento> departamento = departamentoService.obtenerDepartamentoPorNombre(nombre);
        return departamento.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> actualizarDepartamento(@PathVariable Long id, @RequestBody Departamento departamento) {
        Departamento departamentoActualizado = departamentoService.actualizarDepartamento(id, departamento);
        if (departamentoActualizado != null) {
            return new ResponseEntity<>(departamentoActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDepartamento(@PathVariable Long id) {
        if (departamentoService.eliminarDepartamento(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
