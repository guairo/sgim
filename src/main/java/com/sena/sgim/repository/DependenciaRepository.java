package com.sena.sgim.repository;

import com.sena.sgim.model.Dependencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {
    Optional<Dependencia> findByNombreIgnoreCase(String nombre);
    List<Dependencia> findByActivaTrueOrderByNombreAsc();
    boolean existsByNombreIgnoreCase(String nombre);
}
