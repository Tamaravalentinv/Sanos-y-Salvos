package com.sanosysalvos.ms_rh.repository;

import com.sanosysalvos.ms_rh.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    List<Permiso> findByEmpleadoId(Long empleadoId);
    List<Permiso> findByEstado(String estado);
}
