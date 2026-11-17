package com.sena.sgim.repository;

import com.sena.sgim.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    List<Mantenimiento> findByBienIdOrderByFechaProgramadaDesc(Long bienId);
    List<Mantenimiento> findByEstado(Mantenimiento.Estado estado);
}
