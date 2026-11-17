package com.sena.sgim.repository;

import com.sena.sgim.model.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByBienIdOrderByFechaMovimientoDesc(Long bienId);
    Page<Movimiento> findAllByOrderByFechaMovimientoDesc(Pageable pageable);
}
