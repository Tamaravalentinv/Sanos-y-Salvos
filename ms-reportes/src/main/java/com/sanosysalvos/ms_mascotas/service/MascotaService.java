package com.sanosysalvos.ms_mascotas.service;

import com.sanosysalvos.ms_mascotas.model.Mascota;
import com.sanosysalvos.ms_mascotas.repository.MascotaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository repository;

    public MascotaService(MascotaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    public Mascota guardar(@NonNull Mascota mascota) {
        return repository.save(mascota);
    }

    @NonNull
    public List<Mascota> listar() {
        return repository.findAll();
    }
}
