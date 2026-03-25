package com.sanosysalvos.ms_coincidencias.service;

import com.sanosysalvos.ms_coincidencias.model.Mascota;
import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoincidenciaService {

    private final MascotaRepository repository;

    public CoincidenciaService(MascotaRepository repository) {
        this.repository = repository;
    }

    public List<Coincidencia> buscarCoincidencias() {

        List<Mascota> perdidas = repository.findByEstado("PERDIDO");
        List<Mascota> encontradas = repository.findByEstado("ENCONTRADO");

        return perdidas.stream()
                .flatMap(p ->
                        encontradas.stream()
                                .filter(e -> e.getColor().equalsIgnoreCase(p.getColor()))
                                .filter(e -> e.getRaza().equalsIgnoreCase(p.getRaza()))
                                .filter(e -> e.getTipo().equalsIgnoreCase(p.getTipo()))
                                .filter(e -> e.getUbicacion().equalsIgnoreCase(p.getUbicacion()))
                                .map(e -> new Coincidencia(p, e))
                )
                .collect(Collectors.toList());
    }
}