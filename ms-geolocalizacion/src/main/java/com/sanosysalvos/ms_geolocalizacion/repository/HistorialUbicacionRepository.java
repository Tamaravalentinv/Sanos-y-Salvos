package com.sanosysalvos.ms_geolocalizacion.repository;

import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialUbicacionRepository extends JpaRepository<HistorialUbicacion, Long> {
    List<HistorialUbicacion> findByReporteId(Long reporteId);
    List<HistorialUbicacion> findByTipoEvento(String tipoEvento);
    List<HistorialUbicacion> findByQuienReportaId(Long usuarioId);
    List<HistorialUbicacion> findByComprobado(Boolean comprobado);

    @Query("SELECT h FROM HistorialUbicacion h WHERE h.reporteId = :reporteId ORDER BY h.fechaEvento DESC")
    List<HistorialUbicacion> findHistorialPorReporte(@Param("reporteId") Long reporteId);

    @Query("SELECT h FROM HistorialUbicacion h WHERE h.fechaEvento >= :desde ORDER BY h.fechaEvento DESC")
    List<HistorialUbicacion> findRecientes(@Param("desde") LocalDateTime desde);

    @Query("SELECT h FROM HistorialUbicacion h WHERE h.confiabilidad >= :minConfiabilidad ORDER BY h.fechaEvento DESC")
    List<HistorialUbicacion> findPorConfiabilidad(@Param("minConfiabilidad") Integer minConfiabilidad);
}
