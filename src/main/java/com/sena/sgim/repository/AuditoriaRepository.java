package com.sena.sgim.repository;

import com.sena.sgim.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findAllByOrderByFechaDesc(Pageable pageable);
    Page<Auditoria> findByUsuarioOrderByFechaDesc(String usuario, Pageable pageable);
}
