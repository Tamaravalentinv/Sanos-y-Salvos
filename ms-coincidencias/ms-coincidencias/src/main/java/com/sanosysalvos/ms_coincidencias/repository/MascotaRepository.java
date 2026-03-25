package com.sanosysalvos.ms_coincidencias.repository;

import com.sanosysalvos.ms_coincidencias.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByEstado(String estado);
}
