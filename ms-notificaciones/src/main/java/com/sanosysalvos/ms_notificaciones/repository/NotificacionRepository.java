package com.sanosysalvos.ms_notificaciones.repository;

import com.sanosysalvos.ms_notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
    List<Notificacion> findByTipo(String tipo);
    List<Notificacion> findByEstado(String estado);
    List<Notificacion> findByUsuarioIdAndEstado(Long usuarioId, String estado);
    List<Notificacion> findByEventoTipoAndEventoId(String eventoTipo, Long eventoId);
}
