package com.sanosysalvos.ms_usuarios.repository;

import com.sanosysalvos.ms_usuarios.model.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {
    Optional<Organizacion> findByNombre(String nombre);
    Optional<Organizacion> findByRut(String rut);
    List<Organizacion> findByEstado(String estado);
    List<Organizacion> findByTipoId(Long tipoId);
    List<Organizacion> findByEsVerificada(Boolean esVerificada);
}
