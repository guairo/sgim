package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.Categoria;
import com.sena.sgim.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;

    @Transactional(readOnly = true)
    public List<Categoria> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public List<Categoria> listarActivas() { return repo.findByActivaTrueOrderByNombreAsc(); }

    @Transactional(readOnly = true)
    public Categoria obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Categoria " + id));
    }

    @Transactional
    public Categoria crear(Categoria c) {
        if (repo.existsByNombreIgnoreCase(c.getNombre()))
            throw new ReglaNegocioException("Ya existe una categoria con ese nombre");
        c.setId(null);
        return repo.save(c);
    }

    @Transactional
    public Categoria actualizar(Long id, Categoria cambios) {
        Categoria actual = obtener(id);
        actual.setNombre(cambios.getNombre());
        actual.setDescripcion(cambios.getDescripcion());
        actual.setVidaUtilMeses(cambios.getVidaUtilMeses());
        actual.setActiva(cambios.getActiva());
        return actual;
    }

    @Transactional
    public void eliminar(Long id) { repo.delete(obtener(id)); }
}
