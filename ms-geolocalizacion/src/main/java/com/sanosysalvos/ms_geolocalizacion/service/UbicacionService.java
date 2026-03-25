package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public Ubicacion guardar(Ubicacion ubicacion) {
        ubicacion.setFechaRegistro(LocalDateTime.now());
        return ubicacionRepository.save(ubicacion);
    }

    public Ubicacion registrarUbicacionReporte(Long reporteId, Double latitud, Double longitud, 
                                               String descripcion, String tipoEvento) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setReporteId(reporteId);
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        ubicacion.setDescripcion(descripcion);
        ubicacion.setTipoEvento(tipoEvento);
        ubicacion.setFechaRegistro(LocalDateTime.now());
        return ubicacionRepository.save(ubicacion);
    }

    public Optional<Ubicacion> obtenerPorId(Long id) {
        if (id == null) return Optional.empty();
        return ubicacionRepository.findById(id);
    }

    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    public List<Ubicacion> obtenerPorReporte(Long reporteId) {
        return ubicacionRepository.findByReporteId(reporteId);
    }

    public List<Ubicacion> obtenerPorComuna(String comuna) {
        return ubicacionRepository.findByComuna(comuna);
    }

    public List<Ubicacion> obtenerPorCiudad(String ciudad) {
        return ubicacionRepository.findByCiudad(ciudad);
    }

    public List<Ubicacion> obtenerPorCercaniaGeografica(Double latitud, Double longitud, Double radioKm) {
        // Convertir km a grados aproximadamente: 1 grado ≈ 111 km
        Double radioGrados = radioKm / 111.0;
        return ubicacionRepository.findPorCercaniaGeografica(latitud, longitud, radioGrados);
    }

    public List<Ubicacion> obtenerRecientes(Integer diasAtras) {
        LocalDateTime desde = LocalDateTime.now().minusDays(diasAtras);
        return ubicacionRepository.findRecientes(desde);
    }

    public void actualizar(Long id, Ubicacion ubicacion) {
        if (id == null || ubicacion == null) {
            throw new IllegalArgumentException("ID y ubicacion no pueden ser nulos");
        }
        Optional<Ubicacion> existente = ubicacionRepository.findById(id);
        if (existente.isPresent()) {
            Ubicacion u = existente.get();
            if (ubicacion.getDescripcion() != null) u.setDescripcion(ubicacion.getDescripcion());
            if (ubicacion.getLatitud() != null) u.setLatitud(ubicacion.getLatitud());
            if (ubicacion.getLongitud() != null) u.setLongitud(ubicacion.getLongitud());
            Ubicacion actualizada = ubicacionRepository.save(u);
        }
    }

    public void eliminar(Long id) {
        if (id != null) {
            ubicacionRepository.deleteById(id);
        }
    }
}
