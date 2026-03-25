package com.sanosysalvos.ms_geolocalizacion.service;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository repository;

    public UbicacionService(UbicacionRepository repository) {
        this.repository = repository;
    }

    public Ubicacion guardar(Ubicacion ubicacion) {
        return repository.save(ubicacion);
    }

    public List<Ubicacion> listar() {
        return repository.findAll();
    }
}
