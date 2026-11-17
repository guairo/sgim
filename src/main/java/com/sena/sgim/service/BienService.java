package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.*;
import com.sena.sgim.repository.BienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio del modulo central de bienes.
 */
@Service
@RequiredArgsConstructor
public class BienService {

    private final BienRepository repo;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final DependenciaService dependenciaService;
    private final FuncionarioService funcionarioService;
    private final AuditoriaService auditoria;

    @Transactional(readOnly = true)
    public Page<Bien> listar(Pageable p) { return repo.findAll(p); }

    @Transactional(readOnly = true)
    public Page<Bien> buscar(String q, Pageable p) {
        if (q == null || q.isBlank()) return repo.findAll(p);
        return repo.buscar(q.trim(), p);
    }

    @Transactional(readOnly = true)
    public Bien obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Bien " + id));
    }

    @Transactional
    public Bien crear(Bien b, Long categoriaId, Long proveedorId, Long dependenciaId, String usuario) {
        if (repo.existsByPlaca(b.getPlaca()))
            throw new ReglaNegocioException("Placa ya registrada: " + b.getPlaca());
        b.setId(null);
        b.setEstado(Bien.Estado.DISPONIBLE);
        b.setCategoria(categoriaService.obtener(categoriaId));
        if (proveedorId != null) b.setProveedor(proveedorService.obtener(proveedorId));
        b.setDependencia(dependenciaService.obtener(dependenciaId));
        Bien guardado = repo.save(b);
        auditoria.registrar(Auditoria.Accion.CREAR, usuario, "Bien",
                String.valueOf(guardado.getId()), "Alta de bien " + guardado.getPlaca());
        return guardado;
    }

    @Transactional
    public Bien actualizar(Long id, Bien cambios, Long categoriaId, Long proveedorId,
                           Long dependenciaId, String usuario) {
        Bien actual = obtener(id);
        actual.setNombre(cambios.getNombre());
        actual.setDescripcion(cambios.getDescripcion());
        actual.setMarca(cambios.getMarca());
        actual.setModelo(cambios.getModelo());
        actual.setSerial(cambios.getSerial());
        actual.setValorCompra(cambios.getValorCompra());
        actual.setFechaCompra(cambios.getFechaCompra());
        actual.setCategoria(categoriaService.obtener(categoriaId));
        actual.setProveedor(proveedorId != null ? proveedorService.obtener(proveedorId) : null);
        actual.setDependencia(dependenciaService.obtener(dependenciaId));
        auditoria.registrar(Auditoria.Accion.ACTUALIZAR, usuario, "Bien",
                String.valueOf(id), "Actualizacion de bien " + actual.getPlaca());
        return actual;
    }

    @Transactional
    public Bien asignar(Long bienId, Long funcionarioId, String usuario) {
        Bien bien = obtener(bienId);
        if (bien.getEstado() == Bien.Estado.BAJA)
            throw new ReglaNegocioException("No se puede asignar: el bien esta dado de baja");
        if (bien.getEstado() == Bien.Estado.EN_MANTENIMIENTO)
            throw new ReglaNegocioException("No se puede asignar: el bien esta en mantenimiento");
        Funcionario funcionario = funcionarioService.obtener(funcionarioId);
        bien.setFuncionario(funcionario);
        bien.setEstado(Bien.Estado.ASIGNADO);
        auditoria.registrar(Auditoria.Accion.ACTUALIZAR, usuario, "Bien",
                String.valueOf(bienId),
                "Asignacion a funcionario " + funcionario.getDocumento());
        return bien;
    }

    @Transactional
    public Bien liberar(Long bienId, String usuario) {
        Bien bien = obtener(bienId);
        bien.setFuncionario(null);
        if (bien.getEstado() == Bien.Estado.ASIGNADO) bien.setEstado(Bien.Estado.DISPONIBLE);
        auditoria.registrar(Auditoria.Accion.ACTUALIZAR, usuario, "Bien",
                String.valueOf(bienId), "Liberacion de bien");
        return bien;
    }

    @Transactional
    public Bien cambiarEstado(Long bienId, Bien.Estado nuevoEstado, String usuario) {
        Bien bien = obtener(bienId);
        bien.setEstado(nuevoEstado);
        auditoria.registrar(Auditoria.Accion.ACTUALIZAR, usuario, "Bien",
                String.valueOf(bienId), "Cambio de estado a " + nuevoEstado);
        return bien;
    }

    @Transactional
    public void eliminar(Long id, String usuario) {
        Bien b = obtener(id);
        if (b.getEstado() != Bien.Estado.BAJA && b.getEstado() != Bien.Estado.DISPONIBLE)
            throw new ReglaNegocioException("Solo se eliminan bienes DISPONIBLES o en BAJA");
        repo.delete(b);
        auditoria.registrar(Auditoria.Accion.ELIMINAR, usuario, "Bien",
                String.valueOf(id), "Eliminacion de bien " + b.getPlaca());
    }

    @Transactional(readOnly = true)
    public long contarPorEstado(Bien.Estado estado) { return repo.countByEstado(estado); }
}
