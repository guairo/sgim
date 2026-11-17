package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.Proveedor;
import com.sena.sgim.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository repo;

    @Transactional(readOnly = true)
    public List<Proveedor> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() { return repo.findByActivoTrueOrderByRazonSocialAsc(); }

    @Transactional(readOnly = true)
    public Proveedor obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Proveedor " + id));
    }

    @Transactional
    public Proveedor crear(Proveedor p) {
        if (repo.existsByNit(p.getNit()))
            throw new ReglaNegocioException("Ya existe un proveedor con NIT " + p.getNit());
        p.setId(null);
        return repo.save(p);
    }

    @Transactional
    public Proveedor actualizar(Long id, Proveedor cambios) {
        Proveedor actual = obtener(id);
        actual.setRazonSocial(cambios.getRazonSocial());
        actual.setTelefono(cambios.getTelefono());
        actual.setCorreo(cambios.getCorreo());
        actual.setDireccion(cambios.getDireccion());
        actual.setActivo(cambios.getActivo());
        return actual;
    }

    @Transactional
    public void eliminar(Long id) { repo.delete(obtener(id)); }
}
