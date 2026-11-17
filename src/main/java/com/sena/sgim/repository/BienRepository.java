package com.sena.sgim.repository;

import com.sena.sgim.model.Bien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BienRepository extends JpaRepository<Bien, Long> {

    Optional<Bien> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    Page<Bien> findByEstado(Bien.Estado estado, Pageable pageable);

    List<Bien> findByDependenciaId(Long dependenciaId);

    List<Bien> findByFuncionarioId(Long funcionarioId);

    @Query("SELECT b FROM Bien b WHERE " +
            "LOWER(b.placa) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.serial) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Bien> buscar(@Param("q") String q, Pageable pageable);

    long countByEstado(Bien.Estado estado);
}
