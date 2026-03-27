package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import com.sanosysalvos.ms_proyectos.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    
    @Autowired
    private TareaRepository tareaRepository;
    
    public Tarea crearTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }
    
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id);
    }
    
    public List<Tarea> obtenerTodasTareas() {
        return tareaRepository.findAll();
    }
    
    public List<Tarea> obtenerTareasPorProyecto(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId);
    }
    
    public List<Tarea> obtenerTareasPorAsignado(Long asignadoId) {
        return tareaRepository.findByAsignadoId(asignadoId);
    }
    
    public List<Tarea> obtenerTareasPorEstado(String estado) {
        return tareaRepository.findByEstado(estado);
    }
    
    public Tarea actualizarTarea(Long id, Tarea tareaActualizada) {
        Optional<Tarea> tareaOptional = tareaRepository.findById(id);
        if (tareaOptional.isPresent()) {
            Tarea tarea = tareaOptional.get();
            tarea.setTitulo(tareaActualizada.getTitulo());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setEstado(tareaActualizada.getEstado());
            tarea.setPrioridad(tareaActualizada.getPrioridad());
            tarea.setFechaVencimiento(tareaActualizada.getFechaVencimiento());
            tarea.setAsignadoId(tareaActualizada.getAsignadoId());
            return tareaRepository.save(tarea);
        }
        return null;
    }
    
    public boolean eliminarTarea(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
