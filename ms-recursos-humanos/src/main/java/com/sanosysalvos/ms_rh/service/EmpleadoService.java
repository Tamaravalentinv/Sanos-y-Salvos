package com.sanosysalvos.ms_rh.service;

import com.sanosysalvos.ms_rh.model.Empleado;
import com.sanosysalvos.ms_rh.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    public Empleado crearEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }
    
    public Optional<Empleado> obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id);
    }
    
    public Optional<Empleado> obtenerEmpleadoPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }
    
    public Optional<Empleado> obtenerEmpleadoPorCedula(String cedula) {
        return empleadoRepository.findByCedula(cedula);
    }
    
    public List<Empleado> obtenerTodosEmpleados() {
        return empleadoRepository.findAll();
    }
    
    public List<Empleado> obtenerEmpleadosPorDepartamento(Long departamentoId) {
        return empleadoRepository.findByDepartamentoId(departamentoId);
    }
    
    public List<Empleado> obtenerEmpleadosPorEstado(String estado) {
        return empleadoRepository.findByEstado(estado);
    }
    
    public Empleado actualizarEmpleado(Long id, Empleado empleadoActualizado) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        if (empleadoOptional.isPresent()) {
            Empleado empleado = empleadoOptional.get();
            empleado.setNombre(empleadoActualizado.getNombre());
            empleado.setApellido(empleadoActualizado.getApellido());
            empleado.setDepartamentoId(empleadoActualizado.getDepartamentoId());
            empleado.setCargo(empleadoActualizado.getCargo());
            empleado.setSalario(empleadoActualizado.getSalario());
            empleado.setEstado(empleadoActualizado.getEstado());
            return empleadoRepository.save(empleado);
        }
        return null;
    }
    
    public boolean eliminarEmpleado(Long id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
