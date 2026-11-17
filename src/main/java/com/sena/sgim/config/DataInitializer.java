package com.sena.sgim.config;

import com.sena.sgim.model.UsuarioSistema;
import com.sena.sgim.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializa usuarios de prueba con contrasenas BCrypt reales en el perfil dev.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioSistemaRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        upsert("admin",    "Administrador",       "admin@municipio.gov.co",    "admin123", UsuarioSistema.Rol.ADMIN);
        upsert("operador", "Operador inventario", "operador@municipio.gov.co", "oper123",  UsuarioSistema.Rol.OPERADOR);
        upsert("consulta", "Usuario consulta",    "consulta@municipio.gov.co", "cons123",  UsuarioSistema.Rol.CONSULTA);
    }

    private void upsert(String username, String nombre, String correo, String pwd, UsuarioSistema.Rol rol) {
        repo.findByUsername(username).ifPresentOrElse(
                u -> { u.setPassword(encoder.encode(pwd)); u.setActivo(true); repo.save(u); },
                () -> repo.save(UsuarioSistema.builder()
                        .username(username).nombreCompleto(nombre).correo(correo)
                        .password(encoder.encode(pwd)).rol(rol).activo(true).build())
        );
    }
}
