package com.sanosysalvos.ms_coincidencias.controller;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.service.CoincidenciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoincidenciaController {

    private final CoincidenciaService service;

    public CoincidenciaController(CoincidenciaService service) {
        this.service = service;
    }

    @GetMapping("/coincidencias")
    public List<Coincidencia> listar() {
        return service.buscarCoincidencias();
    }
}