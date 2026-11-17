package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.model.Bien;
import com.sena.sgim.model.Mantenimiento;
import com.sena.sgim.repository.MantenimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MantenimientoService {

    private final MantenimientoRepository repo;
    private final BienService bienService;
    private final ProveedorService proveedorService;

    @Transactional(readOnly = true)
    public List<Mantenimiento> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public List<Mantenimiento> historial(Long bienId) {
        return repo.findByBienIdOrderByFechaProgramadaDesc(bienId);
    }

    @Transactional(readOnly = true)
    public Mantenimiento obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento " + id));
    }

    @Transactional
    public Mantenimiento programar(Long bienId, Mantenimiento m, Long proveedorId, String usuario) {
        Bien bien = bienService.obtener(bienId);
        m.setBien(bien);
        m.setEstado(Mantenimiento.Estado.PROGRAMADO);
        if (proveedorId != null) m.setProveedor(proveedorService.obtener(proveedorId));
        return repo.save(m);
    }

    @Transactional
    public Mantenimiento iniciar(Long id, String usuario) {
        Mantenimiento m = obtener(id);
        m.setEstado(Mantenimiento.Estado.EN_EJECUCION);
        bienService.cambiarEstado(m.getBien().getId(), Bien.Estado.EN_MANTENIMIENTO, usuario);
        return m;
    }

    @Transactional
    public Mantenimiento finalizar(Long id, String observaciones, String usuario) {
        Mantenimiento m = obtener(id);
        m.setEstado(Mantenimiento.Estado.FINALIZADO);
        m.setFechaEjecucion(LocalDate.now());
        if (observaciones != null) m.setObservaciones(observaciones);
        bienService.cambiarEstado(m.getBien().getId(), Bien.Estado.DISPONIBLE, usuario);
        return m;
    }
}
