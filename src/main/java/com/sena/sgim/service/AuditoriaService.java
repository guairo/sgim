package com.sena.sgim.service;

import com.sena.sgim.model.Auditoria;
import com.sena.sgim.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository repo;

    @Transactional
    public void registrar(Auditoria.Accion accion, String usuario, String entidad, String entidadId, String detalle) {
        repo.save(Auditoria.builder()
                .accion(accion)
                .usuario(usuario == null ? "SYSTEM" : usuario)
                .entidad(entidad)
                .entidadId(entidadId)
                .detalle(detalle)
                .build());
    }

    @Transactional(readOnly = true)
    public Page<Auditoria> listar(Pageable p) {
        return repo.findAllByOrderByFechaDesc(p);
    }
}
