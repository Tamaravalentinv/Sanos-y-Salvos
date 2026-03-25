package com.sanosysalvos.ms_geolocalizacion.repository;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
