package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.ZonaIncidencia;
import com.sanosysalvos.ms_geolocalizacion.repository.ZonaIncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ZonaIncidenciaService {

    @Autowired
    private ZonaIncidenciaRepository zonaRepository;

    public ZonaIncidencia crearZona(String nombre, Double latitud, Double longitud, Double radioKm) {
        ZonaIncidencia zona = new ZonaIncidencia();
        zona.setNombre(nombre);
        zona.setLatitudCentro(latitud);
        zona.setLongitudCentro(longitud);
        zona.setRadioKm(radioKm);
        zona.setNumIncidencias(0);
        zona.setNivelRiesgo("BAJO");
        zona.setEsActiva(true);
        return zonaRepository.save(zona);
    }

    public Optional<ZonaIncidencia> obtenerPorId(Long id) {
        return zonaRepository.findById(id);
    }

    public Optional<ZonaIncidencia> obtenerPorNombre(String nombre) {
        return zonaRepository.findByNombre(nombre);
    }

    public List<ZonaIncidencia> obtenerTodas() {
        return zonaRepository.findAll();
    }

    public List<ZonaIncidencia> obtenerActivas() {
        return zonaRepository.findByEsActiva(true);
    }

    public List<ZonaIncidencia> obtenerPorNivelRiesgo(String nivelRiesgo) {
        return zonaRepository.findByNivelRiesgo(nivelRiesgo);
    }

    public List<ZonaIncidencia> obtenerZonasAltoRiesgo() {
        return zonaRepository.findZonasAltoRiesgo(5); // Mínimo 5 incidencias
    }

    public List<ZonaIncidencia> obtenerZonasExitosas() {
        return zonaRepository.findZonasExitosas(0.7); // Tasa de recuperación >= 70%
    }

    public void actualizarIncidencias(Long zonaId, Integer numPerdidas, Integer numEncontradas) {
        Optional<ZonaIncidencia> zona = zonaRepository.findById(zonaId);
        if (zona.isPresent()) {
            ZonaIncidencia z = zona.get();
            z.setNumIncidencias(z.getNumIncidencias() + 1);
            z.setNumPerdidas(z.getNumPerdidas() + numPerdidas);
            z.setNumEncontradas(z.getNumEncontradas() + numEncontradas);
            
            // Calcular tasa de recuperación
            if (z.getNumPerdidas() > 0) {
                z.setTasaRecuperacion((double) z.getNumEncontradas() / z.getNumPerdidas());
            }
            
            // Actualizar nivel de riesgo
            actualizarNivelRiesgo(z);
            z.setFechaUltimaActualizacion(LocalDateTime.now());
            zonaRepository.save(z);
        }
    }

    private void actualizarNivelRiesgo(ZonaIncidencia zona) {
        if (zona.getNumIncidencias() < 5) {
            zona.setNivelRiesgo("BAJO");
        } else if (zona.getNumIncidencias() < 15) {
            zona.setNivelRiesgo("MEDIO");
        } else if (zona.getNumIncidencias() < 30) {
            zona.setNivelRiesgo("ALTO");
        } else {
            zona.setNivelRiesgo("CRITICO");
        }
    }

    public void eliminarZona(Long id) {
        zonaRepository.deleteById(id);
    }
}
