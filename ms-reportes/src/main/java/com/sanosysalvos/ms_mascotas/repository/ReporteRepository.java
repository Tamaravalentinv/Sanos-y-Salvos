package com.sanosysalvos.ms_mascotas.repository;

import com.sanosysalvos.ms_mascotas.model.Reporte;
import com.sanosysalvos.ms_mascotas.model.TipoReporte;
import com.sanosysalvos.ms_mascotas.model.EstadoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipo(TipoReporte tipo);
    List<Reporte> findByEstado(EstadoReporte estado);
    List<Reporte> findByTipoAndEstado(TipoReporte tipo, EstadoReporte estado);
    List<Reporte> findByUsuarioId(Long usuarioId);
    List<Reporte> findByOrganizacionId(Long organizacionId);

    @Query("SELECT r FROM Reporte r WHERE r.tipo = :tipo AND r.estado = :estado AND r.fechaCreacion >= :fechaDesde")
    List<Reporte> findRecientes(@Param("tipo") TipoReporte tipo, @Param("estado") EstadoReporte estado, @Param("fechaDesde") LocalDateTime fechaDesde);

    @Query("SELECT r FROM Reporte r WHERE r.tipo = :tipo AND r.mascota.color = :color AND r.mascota.raza = :raza")
    List<Reporte> findByCaracteristicas(@Param("tipo") TipoReporte tipo, @Param("color") String color, @Param("raza") String raza);

    @Query("SELECT r FROM Reporte r WHERE ABS(r.latitud - :latitud) <= 0.01 AND ABS(r.longitud - :longitud) <= 0.01")
    List<Reporte> findPorCercaniaGeografica(@Param("latitud") Double latitud, @Param("longitud") Double longitud);

    List<Reporte> findByMascotaTipo(String tipo);
    List<Reporte> findByPrioridad(Integer prioridad);
    List<Reporte> findByRequiereUrgencia(Boolean requiereUrgencia);
}
