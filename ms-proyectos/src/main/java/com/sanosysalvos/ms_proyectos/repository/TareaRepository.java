package com.sanosysalvos.ms_proyectos.repository;

import com.sanosysalvos.ms_proyectos.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyectoId(Long proyectoId);
    List<Tarea> findByAsignadoId(Long asignadoId);
    List<Tarea> findByEstado(String estado);
}
