package com.sanosysalvos.ms_geolocalizacion.repository;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByReporteId(Long reporteId);
    List<Ubicacion> findByTipoEvento(String tipoEvento);
    List<Ubicacion> findByComuna(String comuna);
    List<Ubicacion> findByCiudad(String ciudad);
    List<Ubicacion> findByRegion(String region);

    @Query("SELECT u FROM Ubicacion u WHERE ABS(u.latitud - :latitud) <= :radioGrados AND ABS(u.longitud - :longitud) <= :radioGrados")
    List<Ubicacion> findPorCercaniaGeografica(@Param("latitud") Double latitud, @Param("longitud") Double longitud, @Param("radioGrados") Double radioGrados);

    @Query("SELECT u FROM Ubicacion u WHERE u.fechaRegistro >= :desde ORDER BY u.fechaRegistro DESC")
    List<Ubicacion> findRecientes(@Param("desde") LocalDateTime desde);
}
