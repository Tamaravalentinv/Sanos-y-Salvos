package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Rol;
import com.sanosysalvos.ms_usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol crearRol(String nombre, String descripcion) {
        if (rolRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("El rol ya existe");
        }
        return rolRepository.save(new Rol(nombre, descripcion));
    }

    public Optional<Rol> obtenerPorId(Long id) {
        return rolRepository.findById(id);
    }

    public Optional<Rol> obtenerPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}
