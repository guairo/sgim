package com.sena.sgim.repository;

import com.sena.sgim.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNit(String nit);
    List<Proveedor> findByActivoTrueOrderByRazonSocialAsc();
    boolean existsByNit(String nit);
}
