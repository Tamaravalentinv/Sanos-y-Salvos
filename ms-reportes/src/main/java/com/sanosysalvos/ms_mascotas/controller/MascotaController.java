package com.sanosysalvos.ms_mascotas.controller;

import com.sanosysalvos.ms_mascotas.model.Mascota;
import com.sanosysalvos.ms_mascotas.service.MascotaService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
    }

    @GetMapping
    @NonNull
    public List<Mascota> listar() {
        return service.listar();
    }

    @PostMapping
    @NonNull
    public Mascota crear(@RequestBody @NonNull Mascota mascota) {
        return service.guardar(mascota);
    }
}

