package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;
    
    public Proyecto crearProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }
    
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return proyectoRepository.findById(id);
    }
    
    public List<Proyecto> obtenerTodosProyectos() {
        return proyectoRepository.findAll();
    }
    
    public List<Proyecto> obtenerProyectosPorResponsable(Long responsableId) {
        return proyectoRepository.findByResponsableId(responsableId);
    }
    
    public List<Proyecto> obtenerProyectosPorEstado(String estado) {
        return proyectoRepository.findByEstado(estado);
    }
    
    public Proyecto actualizarProyecto(Long id, Proyecto proyectoActualizado) {
        Optional<Proyecto> proyectoOptional = proyectoRepository.findById(id);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();
            proyecto.setNombre(proyectoActualizado.getNombre());
            proyecto.setDescripcion(proyectoActualizado.getDescripcion());
            proyecto.setEstado(proyectoActualizado.getEstado());
            proyecto.setFechaFin(proyectoActualizado.getFechaFin());
            proyecto.setPresupuesto(proyectoActualizado.getPresupuesto());
            proyecto.setResponsableId(proyectoActualizado.getResponsableId());
            return proyectoRepository.save(proyecto);
        }
        return null;
    }
    
    public boolean eliminarProyecto(Long id) {
        if (proyectoRepository.existsById(id)) {
            proyectoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
