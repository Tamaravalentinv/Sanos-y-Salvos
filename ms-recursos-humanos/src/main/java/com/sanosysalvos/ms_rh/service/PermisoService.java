package com.sanosysalvos.ms_rh.service;

import com.sanosysalvos.ms_rh.model.Permiso;
import com.sanosysalvos.ms_rh.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PermisoService {
    
    @Autowired
    private PermisoRepository permisoRepository;
    
    public Permiso crearPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }
    
    public Optional<Permiso> obtenerPermisoPorId(Long id) {
        return permisoRepository.findById(id);
    }
    
    public List<Permiso> obtenerTodosPermisos() {
        return permisoRepository.findAll();
    }
    
    public List<Permiso> obtenerPermisosPorEmpleado(Long empleadoId) {
        return permisoRepository.findByEmpleadoId(empleadoId);
    }
    
    public List<Permiso> obtenerPermisosPorEstado(String estado) {
        return permisoRepository.findByEstado(estado);
    }
    
    public Permiso actualizarPermiso(Long id, Permiso permisoActualizado) {
        Optional<Permiso> permisoOptional = permisoRepository.findById(id);
        if (permisoOptional.isPresent()) {
            Permiso permiso = permisoOptional.get();
            permiso.setTipo(permisoActualizado.getTipo());
            permiso.setFechaInicio(permisoActualizado.getFechaInicio());
            permiso.setFechaFin(permisoActualizado.getFechaFin());
            permiso.setDescripcion(permisoActualizado.getDescripcion());
            permiso.setEstado(permisoActualizado.getEstado());
            return permisoRepository.save(permiso);
        }
        return null;
    }
    
    public boolean eliminarPermiso(Long id) {
        if (permisoRepository.existsById(id)) {
            permisoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
