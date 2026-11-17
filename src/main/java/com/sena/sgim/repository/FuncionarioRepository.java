package com.sena.sgim.repository;

import com.sena.sgim.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByDocumento(String documento);
    List<Funcionario> findByDependenciaIdAndActivoTrue(Long dependenciaId);
    List<Funcionario> findByActivoTrueOrderByApellidosAscNombresAsc();
    boolean existsByDocumento(String documento);
}
