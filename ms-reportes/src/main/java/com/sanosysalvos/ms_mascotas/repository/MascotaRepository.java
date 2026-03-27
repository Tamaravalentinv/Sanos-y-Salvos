package com.sanosysalvos.ms_mascotas.repository;

import com.sanosysalvos.ms_mascotas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByTipo(String tipo);
    List<Mascota> findByRaza(String raza);
    List<Mascota> findByColor(String color);
    List<Mascota> findByNombre(String nombre);
}
