package com.sanosysalvos.ms_usuarios.service;

import com.sanosysalvos.ms_usuarios.model.Organizacion;
import com.sanosysalvos.ms_usuarios.model.TipoOrganizacion;
import com.sanosysalvos.ms_usuarios.repository.OrganizacionRepository;
import com.sanosysalvos.ms_usuarios.repository.TipoOrganizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizacionService {

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Autowired
    private TipoOrganizacionRepository tipoOrganizacionRepository;

    // Factory Method Pattern: Crear organización según el tipo
    public Organizacion crearOrganizacion(String nombre, Long tipoId, String descripcion) {
        Optional<TipoOrganizacion> tipo = tipoOrganizacionRepository.findById(tipoId);
        if (!tipo.isPresent()) {
            throw new RuntimeException("Tipo de organización no encontrado");
        }

        Organizacion organizacion = new Organizacion();
        organizacion.setNombre(nombre);
        organizacion.setTipo(tipo.get());
        organizacion.setDescripcion(descripcion);
        organizacion.setFechaRegistro(LocalDateTime.now());
        organizacion.setEstado("ACTIVO");
        organizacion.setEsVerificada(false);

        return organizacionRepository.save(organizacion);
    }

    public Optional<Organizacion> obtenerPorId(Long id) {
        return organizacionRepository.findById(id);
    }

    public Optional<Organizacion> obtenerPorNombre(String nombre) {
        return organizacionRepository.findByNombre(nombre);
    }

    public Optional<Organizacion> obtenerPorRut(String rut) {
        return organizacionRepository.findByRut(rut);
    }

    public List<Organizacion> obtenerTodas() {
        return organizacionRepository.findAll();
    }

    public List<Organizacion> obtenerPorEstado(String estado) {
        return organizacionRepository.findByEstado(estado);
    }

    public List<Organizacion> obtenerPorTipo(Long tipoId) {
        return organizacionRepository.findByTipoId(tipoId);
    }

    public List<Organizacion> obtenerVerificadas() {
        return organizacionRepository.findByEsVerificada(true);
    }

    public Organizacion actualizarOrganizacion(Long id, Organizacion organizacion) {
        Optional<Organizacion> orgExistente = organizacionRepository.findById(id);
        if (orgExistente.isPresent()) {
            Organizacion org = orgExistente.get();
            if (organizacion.getNombre() != null) org.setNombre(organizacion.getNombre());
            if (organizacion.getDescripcion() != null) org.setDescripcion(organizacion.getDescripcion());
            if (organizacion.getRut() != null) org.setRut(organizacion.getRut());
            if (organizacion.getEstado() != null) org.setEstado(organizacion.getEstado());
            if (organizacion.getContacto() != null) org.setContacto(organizacion.getContacto());
            return organizacionRepository.save(org);
        }
        throw new RuntimeException("Organización no encontrada");
    }

    public void verificarOrganizacion(Long id) {
        Optional<Organizacion> org = organizacionRepository.findById(id);
        if (org.isPresent()) {
            Organizacion o = org.get();
            o.setEsVerificada(true);
            organizacionRepository.save(o);
        }
    }

    public void eliminarOrganizacion(Long id) {
        organizacionRepository.deleteById(id);
    }
}
