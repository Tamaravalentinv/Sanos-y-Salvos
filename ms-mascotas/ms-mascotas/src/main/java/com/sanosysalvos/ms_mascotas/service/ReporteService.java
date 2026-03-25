package com.sanosysalvos.ms_mascotas.service;

import com.sanosysalvos.ms_mascotas.model.*;
import com.sanosysalvos.ms_mascotas.repository.ReporteRepository;
import com.sanosysalvos.ms_mascotas.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    // Factory Method Pattern: Crear reporte según tipo
    public Reporte crearReporte(TipoReporte tipo, Long usuarioId, Mascota mascota, 
                               String ubicacion, Double latitud, Double longitud,
                               LocalDateTime fechaIncidente, String descripcion,
                               String telefonoContacto, String emailContacto) {
        
        Reporte reporte = new Reporte();
        reporte.setTipo(tipo);
        reporte.setUsuarioId(usuarioId);
        reporte.setMascota(mascota);
        reporte.setUbicacion(ubicacion);
        reporte.setLatitud(latitud);
        reporte.setLongitud(longitud);
        reporte.setFechaIncidente(fechaIncidente);
        reporte.setDescripcion(descripcion);
        reporte.setTelefonoContacto(telefonoContacto);
        reporte.setEmailContacto(emailContacto);
        reporte.setEstado(EstadoReporte.ABIERTO);
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setNumVisualizaciones(0);
        reporte.setPrioridad(1);

        // Guardar mascota primero
        mascota = mascotaRepository.save(mascota);
        reporte.setMascota(mascota);

        return reporteRepository.save(reporte);
    }

    public Optional<Reporte> obtenerPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }

    public List<Reporte> obtenerPorTipo(TipoReporte tipo) {
        return reporteRepository.findByTipo(tipo);
    }

    public List<Reporte> obtenerPorEstado(EstadoReporte estado) {
        return reporteRepository.findByEstado(estado);
    }

    public List<Reporte> obtenerPerdidos() {
        return reporteRepository.findByTipo(TipoReporte.PERDIDA);
    }

    public List<Reporte> obtenerEncontrados() {
        return reporteRepository.findByTipo(TipoReporte.ENCONTRADA);
    }

    public List<Reporte> obtenerActivos() {
        return reporteRepository.findByTipoAndEstado(TipoReporte.PERDIDA, EstadoReporte.ABIERTO);
    }

    public List<Reporte> obtenerPorUsuario(Long usuarioId) {
        return reporteRepository.findByUsuarioId(usuarioId);
    }

    public List<Reporte> obtenerPorOrganizacion(Long organizacionId) {
        return reporteRepository.findByOrganizacionId(organizacionId);
    }

    public List<Reporte> obtenerRecientes(TipoReporte tipo, LocalDateTime desde) {
        return reporteRepository.findRecientes(tipo, EstadoReporte.ABIERTO, desde);
    }

    public List<Reporte> obtenerPorCaracteristicas(TipoReporte tipo, String color, String raza) {
        return reporteRepository.findByCaracteristicas(tipo, color, raza);
    }

    public List<Reporte> obtenerPorCercaniaGeografica(Double latitud, Double longitud) {
        return reporteRepository.findPorCercaniaGeografica(latitud, longitud);
    }

    public List<Reporte> obtenerPorUrgencia() {
        return reporteRepository.findByRequiereUrgencia(true);
    }

    public Reporte actualizarReporte(Long id, Reporte reporte) {
        Optional<Reporte> reporteExistente = reporteRepository.findById(id);
        if (reporteExistente.isPresent()) {
            Reporte r = reporteExistente.get();
            if (reporte.getEstado() != null) r.setEstado(reporte.getEstado());
            if (reporte.getDescripcion() != null) r.setDescripcion(reporte.getDescripcion());
            if (reporte.getPrioridad() != null) r.setPrioridad(reporte.getPrioridad());
            if (reporte.getUbicacion() != null) r.setUbicacion(reporte.getUbicacion());
            if (reporte.getLatitud() != null) r.setLatitud(reporte.getLatitud());
            if (reporte.getLongitud() != null) r.setLongitud(reporte.getLongitud());
            return reporteRepository.save(r);
        }
        throw new RuntimeException("Reporte no encontrado");
    }

    public void cambiarEstado(Long id, EstadoReporte nuevoEstado) {
        Optional<Reporte> reporte = reporteRepository.findById(id);
        if (reporte.isPresent()) {
            Reporte r = reporte.get();
            r.setEstado(nuevoEstado);
            if (EstadoReporte.RESUELTO.equals(nuevoEstado) || EstadoReporte.CERRADO.equals(nuevoEstado)) {
                r.setFechaResolucion(LocalDateTime.now());
            }
            reporteRepository.save(r);
        }
    }

    public void incrementarVisualizaciones(Long id) {
        Optional<Reporte> reporte = reporteRepository.findById(id);
        if (reporte.isPresent()) {
            Reporte r = reporte.get();
            r.setNumVisualizaciones(r.getNumVisualizaciones() + 1);
            reporteRepository.save(r);
        }
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}
