package com.sena.sgim.service;

import com.sena.sgim.model.Bien;
import com.sena.sgim.model.Movimiento;
import com.sena.sgim.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository repo;
    private final BienService bienService;
    private final DependenciaService dependenciaService;
    private final FuncionarioService funcionarioService;

    @Transactional(readOnly = true)
    public Page<Movimiento> listar(Pageable p) { return repo.findAllByOrderByFechaMovimientoDesc(p); }

    @Transactional(readOnly = true)
    public List<Movimiento> historial(Long bienId) {
        return repo.findByBienIdOrderByFechaMovimientoDesc(bienId);
    }

    @Transactional
    public Movimiento registrar(Movimiento.Tipo tipo, Long bienId, Long depOrigenId, Long depDestinoId,
                                Long funcOrigenId, Long funcDestinoId, String observaciones, String usuario) {
        Bien bien = bienService.obtener(bienId);
        Movimiento m = Movimiento.builder()
                .tipo(tipo)
                .bien(bien)
                .observaciones(observaciones)
                .usuarioRegistro(usuario)
                .build();
        if (depOrigenId != null)   m.setDependenciaOrigen(dependenciaService.obtener(depOrigenId));
        if (depDestinoId != null)  m.setDependenciaDestino(dependenciaService.obtener(depDestinoId));
        if (funcOrigenId != null)  m.setFuncionarioOrigen(funcionarioService.obtener(funcOrigenId));
        if (funcDestinoId != null) m.setFuncionarioDestino(funcionarioService.obtener(funcDestinoId));
        return repo.save(m);
    }
}
