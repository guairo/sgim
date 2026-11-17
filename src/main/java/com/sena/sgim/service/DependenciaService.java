package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.Auditoria;
import com.sena.sgim.model.Dependencia;
import com.sena.sgim.repository.DependenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DependenciaService {

    private final DependenciaRepository repo;
    private final AuditoriaService auditoria;

    @Transactional(readOnly = true)
    public List<Dependencia> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Dependencia> listarActivas() {
        return repo.findByActivaTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Dependencia obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(
                "Dependencia " + id + " no existe"));
    }

    @Transactional
    public Dependencia crear(Dependencia d, String usuario) {
        if (repo.existsByNombreIgnoreCase(d.getNombre())) {
            throw new ReglaNegocioException("Ya existe una dependencia con ese nombre");
        }
        d.setId(null);
        Dependencia guardada = repo.save(d);
        auditoria.registrar(Auditoria.Accion.CREAR, usuario, "Dependencia",
                String.valueOf(guardada.getId()), "Creacion de dependencia " + guardada.getNombre());
        return guardada;
    }

    @Transactional
    public Dependencia actualizar(Long id, Dependencia cambios, String usuario) {
        Dependencia actual = obtener(id);
        actual.setNombre(cambios.getNombre());
        actual.setDescripcion(cambios.getDescripcion());
        actual.setUbicacion(cambios.getUbicacion());
        actual.setActiva(cambios.getActiva());
        auditoria.registrar(Auditoria.Accion.ACTUALIZAR, usuario, "Dependencia",
                String.valueOf(id), "Actualizacion de dependencia " + actual.getNombre());
        return actual;
    }

    @Transactional
    public void eliminar(Long id, String usuario) {
        Dependencia d = obtener(id);
        if (!d.getBienes().isEmpty()) {
            throw new ReglaNegocioException("No se puede eliminar: la dependencia tiene bienes asociados");
        }
        repo.delete(d);
        auditoria.registrar(Auditoria.Accion.ELIMINAR, usuario, "Dependencia",
                String.valueOf(id), "Eliminacion de dependencia " + d.getNombre());
    }
}
