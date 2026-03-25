package com.sanosysalvos.ms_geolocalizacion.repository;

import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaIncidenciaRepository extends JpaRepository<ZonaIncidencia, Long> {
    Optional<ZonaIncidencia> findByNombre(String nombre);
    List<ZonaIncidencia> findByNivelRiesgo(String nivelRiesgo);
    List<ZonaIncidencia> findByEsActiva(Boolean esActiva);

    @Query("SELECT z FROM ZonaIncidencia z WHERE z.tasaRecuperacion >= :minTasa ORDER BY z.tasaRecuperacion DESC")
    List<ZonaIncidencia> findZonasExitosas(@Param("minTasa") Double minTasa);

    @Query("SELECT z FROM ZonaIncidencia z WHERE z.numIncidencias >= :minIncidencias AND z.nivelRiesgo IN ('ALTO', 'CRITICO') ORDER BY z.numIncidencias DESC")
    List<ZonaIncidencia> findZonasAltoRiesgo(@Param("minIncidencias") Integer minIncidencias);
}
