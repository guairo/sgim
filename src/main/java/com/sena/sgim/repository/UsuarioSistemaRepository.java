package com.sena.sgim.repository;

import com.sena.sgim.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    Optional<UsuarioSistema> findByUsername(String username);
    Optional<UsuarioSistema> findByCorreo(String correo);
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);
}
