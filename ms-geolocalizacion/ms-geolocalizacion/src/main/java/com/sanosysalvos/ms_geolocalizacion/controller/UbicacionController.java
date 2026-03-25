package com.sanosysalvos.ms_geolocalizacion.controller;

import com.sanosysalvos.ms_geolocalizacion.model.Ubicacion;
import com.sanosysalvos.ms_geolocalizacion.service.UbicacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {

    private final UbicacionService service;

    public UbicacionController(UbicacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ubicacion> listar() {
        return service.listar();
    }

    @PostMapping
    public Ubicacion crear(@RequestBody Ubicacion ubicacion) {
        return service.guardar(ubicacion);
    }
}
