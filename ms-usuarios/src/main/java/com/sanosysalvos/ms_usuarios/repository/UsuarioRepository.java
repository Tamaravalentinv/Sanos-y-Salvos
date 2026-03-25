package com.sanosysalvos.ms_usuarios.repository;

import com.sanosysalvos.ms_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByEstado(String estado);
    List<Usuario> findByOrganizacionId(Long organizacionId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
