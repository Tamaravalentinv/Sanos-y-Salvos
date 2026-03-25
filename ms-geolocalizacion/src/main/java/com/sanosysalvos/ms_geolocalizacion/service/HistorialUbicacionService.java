package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.HistorialUbicacion;
import com.sanosysalvos.ms_geolocalizacion.repository.HistorialUbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialUbicacionService {

    @Autowired
    private HistorialUbicacionRepository historialRepository;

    public HistorialUbicacion registrarAvistamiento(Long reporteId, Double latitud, Double longitud,
                                                    String descripcion, Long quienReporta, 
                                                    String fuenteInformacion, Integer confiabilidad) {
        HistorialUbicacion historial = new HistorialUbicacion();
        historial.setReporteId(reporteId);
        historial.setLatitud(latitud);
        historial.setLongitud(longitud);
        historial.setDescripcion(descripcion);
        historial.setTipoEvento("AVISTAMIENTO");
        historial.setQuienReportaId(quienReporta);
        historial.setFuenteInformacion(fuenteInformacion);
        historial.setConfiabilidad(confiabilidad);
        historial.setFechaEvento(LocalDateTime.now());
        return historialRepository.save(historial);
    }

    public HistorialUbicacion registrarHallazgo(Long reporteId, Double latitud, Double longitud,
                                               String descripcion, Long quienReporta) {
        HistorialUbicacion historial = new HistorialUbicacion();
        historial.setReporteId(reporteId);
        historial.setLatitud(latitud);
        historial.setLongitud(longitud);
        historial.setDescripcion(descripcion);
        historial.setTipoEvento("HALLAZGO");
        historial.setQuienReportaId(quienReporta);
        historial.setFuenteInformacion("USUARIO");
        historial.setConfiabilidad(5); // Máxima confiabilidad
        historial.setFechaEvento(LocalDateTime.now());
        historial.setComprobado(true);
        return historialRepository.save(historial);
    }

    public Optional<HistorialUbicacion> obtenerPorId(Long id) {
        return historialRepository.findById(id);
    }

    public List<HistorialUbicacion> obtenerHistorialPorReporte(Long reporteId) {
        return historialRepository.findHistorialPorReporte(reporteId);
    }

    public List<HistorialUbicacion> obtenerPorTipoEvento(String tipoEvento) {
        return historialRepository.findByTipoEvento(tipoEvento);
    }

    public List<HistorialUbicacion> obtenerPorUsuario(Long usuarioId) {
        return historialRepository.findByQuienReportaId(usuarioId);
    }

    public List<HistorialUbicacion> obtenerNoComprobados() {
        return historialRepository.findByComprobado(false);
    }

    public List<HistorialUbicacion> obtenerRecientes(Integer diasAtras) {
        LocalDateTime desde = LocalDateTime.now().minusDays(diasAtras);
        return historialRepository.findRecientes(desde);
    }

    public List<HistorialUbicacion> obtenerPorConfiabilidad(Integer minConfiabilidad) {
        return historialRepository.findPorConfiabilidad(minConfiabilidad);
    }

    public void verificar(Long id) {
        Optional<HistorialUbicacion> historial = historialRepository.findById(id);
        if (historial.isPresent()) {
            HistorialUbicacion h = historial.get();
            h.setComprobado(true);
            historialRepository.save(h);
        }
    }

    public void eliminar(Long id) {
        historialRepository.deleteById(id);
    }
}
