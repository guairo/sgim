package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.Funcionario;
import com.sena.sgim.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repo;
    private final DependenciaService dependenciaService;

    @Transactional(readOnly = true)
    public List<Funcionario> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public List<Funcionario> listarActivos() { return repo.findByActivoTrueOrderByApellidosAscNombresAsc(); }

    @Transactional(readOnly = true)
    public Funcionario obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Funcionario " + id));
    }

    @Transactional
    public Funcionario crear(Funcionario f, Long dependenciaId) {
        if (repo.existsByDocumento(f.getDocumento()))
            throw new ReglaNegocioException("Documento ya registrado: " + f.getDocumento());
        f.setId(null);
        f.setDependencia(dependenciaService.obtener(dependenciaId));
        return repo.save(f);
    }

    @Transactional
    public Funcionario actualizar(Long id, Funcionario cambios, Long dependenciaId) {
        Funcionario actual = obtener(id);
        actual.setNombres(cambios.getNombres());
        actual.setApellidos(cambios.getApellidos());
        actual.setCargo(cambios.getCargo());
        actual.setCorreo(cambios.getCorreo());
        actual.setTelefono(cambios.getTelefono());
        actual.setActivo(cambios.getActivo());
        actual.setDependencia(dependenciaService.obtener(dependenciaId));
        return actual;
    }

    @Transactional
    public void eliminar(Long id) { repo.delete(obtener(id)); }
}
