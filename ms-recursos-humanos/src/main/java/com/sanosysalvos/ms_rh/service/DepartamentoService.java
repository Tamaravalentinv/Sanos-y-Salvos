package com.sanosysalvos.ms_rh.service;

import com.sanosysalvos.ms_rh.model.Departamento;
import com.sanosysalvos.ms_rh.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    
    public Departamento crearDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }
    
    public Optional<Departamento> obtenerDepartamentoPorId(Long id) {
        return departamentoRepository.findById(id);
    }
    
    public Optional<Departamento> obtenerDepartamentoPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }
    
    public List<Departamento> obtenerTodosDepartamentos() {
        return departamentoRepository.findAll();
    }
    
    public Departamento actualizarDepartamento(Long id, Departamento departamentoActualizado) {
        Optional<Departamento> departamentoOptional = departamentoRepository.findById(id);
        if (departamentoOptional.isPresent()) {
            Departamento departamento = departamentoOptional.get();
            departamento.setNombre(departamentoActualizado.getNombre());
            departamento.setDescripcion(departamentoActualizado.getDescripcion());
            departamento.setGerentId(departamentoActualizado.getGerentId());
            departamento.setEstado(departamentoActualizado.getEstado());
            return departamentoRepository.save(departamento);
        }
        return null;
    }
    
    public boolean eliminarDepartamento(Long id) {
        if (departamentoRepository.existsById(id)) {
            departamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
