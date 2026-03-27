package com.sanosysalvos.ms_coincidencias.repository;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoincidenciaRepository extends JpaRepository<Coincidencia, Long> {

    @Query("SELECT c FROM Coincidencia c WHERE c.reportePerdidoId = :reporteId OR c.reporteEncontradoId = :reporteId")
    List<Coincidencia> findByReporteId(@Param("reporteId") Long reporteId);

    @Query("SELECT c FROM Coincidencia c WHERE c.reportePerdidoId = :reportePerdidoId AND c.reporteEncontradoId = :reporteEncontradoId")
    Coincidencia findByReportePerdidoIdAndReporteEncontradoId(
        @Param("reportePerdidoId") Long reportePerdidoId,
        @Param("reporteEncontradoId") Long reporteEncontradoId
    );

    @Query("SELECT c FROM Coincidencia c WHERE c.estado = :estado")
    List<Coincidencia> findByEstado(@Param("estado") Coincidencia.EstadoCoincidencia estado);

    @Query("SELECT c FROM Coincidencia c WHERE c.estado = :estado ORDER BY c.puntajeTotal DESC")
    List<Coincidencia> findByEstadoOrderByPuntaje(@Param("estado") Coincidencia.EstadoCoincidencia estado);

    @Query("SELECT c FROM Coincidencia c WHERE c.puntajeTotal >= :puntajeMinimo ORDER BY c.puntajeTotal DESC")
    List<Coincidencia> findPotentialMatches(@Param("puntajeMinimo") Double puntajeMinimo);

    @Query("SELECT c FROM Coincidencia c WHERE c.estado = 'CONFIRMADA' ORDER BY c.fechaConfirmacion DESC")
    List<Coincidencia> findConfirmedMatches();

    @Query("SELECT c FROM Coincidencia c WHERE c.fechaAnalisis >= :fechaInicio ORDER BY c.puntajeTotal DESC")
    List<Coincidencia> findRecentMatches(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT c FROM Coincidencia c WHERE c.usuarioQuienConfirmo = :usuarioId ORDER BY c.fechaConfirmacion DESC")
    List<Coincidencia> findConfirmationsByUser(@Param("usuarioId") Long usuarioId);
}
