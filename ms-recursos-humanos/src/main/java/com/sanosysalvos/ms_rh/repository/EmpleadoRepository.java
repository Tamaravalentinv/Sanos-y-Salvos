package com.sanosysalvos.ms_rh.repository;

import com.sanosysalvos.ms_rh.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    Optional<Empleado> findByCedula(String cedula);
    List<Empleado> findByDepartamentoId(Long departamentoId);
    List<Empleado> findByEstado(String estado);
}
