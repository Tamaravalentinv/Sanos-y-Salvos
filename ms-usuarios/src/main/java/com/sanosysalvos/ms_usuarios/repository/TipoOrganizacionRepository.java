package com.sanosysalvos.ms_usuarios.repository;

import com.sanosysalvos.ms_usuarios.model.TipoOrganizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoOrganizacionRepository extends JpaRepository<TipoOrganizacion, Long> {
    Optional<TipoOrganizacion> findByNombre(String nombre);
}
