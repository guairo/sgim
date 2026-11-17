package com.sena.sgim.service;

import com.sena.sgim.exception.RecursoNoEncontradoException;
import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.UsuarioSistema;
import com.sena.sgim.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioSistemaService {

    private final UsuarioSistemaRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioSistema> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public UsuarioSistema obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario " + id));
    }

    @Transactional
    public UsuarioSistema crear(UsuarioSistema u, String passwordPlano) {
        if (repo.existsByUsername(u.getUsername()))
            throw new ReglaNegocioException("Username ya existe: " + u.getUsername());
        if (repo.existsByCorreo(u.getCorreo()))
            throw new ReglaNegocioException("Correo ya registrado: " + u.getCorreo());
        u.setId(null);
        u.setPassword(passwordEncoder.encode(passwordPlano));
        return repo.save(u);
    }

    @Transactional
    public UsuarioSistema actualizar(Long id, UsuarioSistema cambios, String nuevoPassword) {
        UsuarioSistema actual = obtener(id);
        actual.setNombreCompleto(cambios.getNombreCompleto());
        actual.setCorreo(cambios.getCorreo());
        actual.setRol(cambios.getRol());
        actual.setActivo(cambios.getActivo());
        if (nuevoPassword != null && !nuevoPassword.isBlank()) {
            actual.setPassword(passwordEncoder.encode(nuevoPassword));
        }
        return actual;
    }

    @Transactional
    public void registrarAcceso(String username) {
        repo.findByUsername(username).ifPresent(u -> u.setUltimoAcceso(LocalDateTime.now()));
    }

    @Transactional
    public void eliminar(Long id) { repo.delete(obtener(id)); }
}
