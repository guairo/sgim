package com.sena.sgim.service;

import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.UsuarioSistema;
import com.sena.sgim.repository.UsuarioSistemaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSistemaServiceTest {

    @Mock private UsuarioSistemaRepository repo;
    @Mock private PasswordEncoder encoder;
    @InjectMocks private UsuarioSistemaService service;

    @Test
    void crear_encripta_password() {
        UsuarioSistema u = UsuarioSistema.builder()
                .username("juan").correo("j@x.co").nombreCompleto("Juan")
                .rol(UsuarioSistema.Rol.OPERADOR).activo(true).build();
        when(repo.existsByUsername("juan")).thenReturn(false);
        when(repo.existsByCorreo("j@x.co")).thenReturn(false);
        when(encoder.encode("secret")).thenReturn("$2a$encoded$");
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UsuarioSistema creado = service.crear(u, "secret");

        assertThat(creado.getPassword()).isEqualTo("$2a$encoded$");
        verify(encoder).encode("secret");
    }

    @Test
    void crear_falla_si_username_existe() {
        UsuarioSistema u = UsuarioSistema.builder()
                .username("admin").correo("a@x.co").nombreCompleto("X").build();
        when(repo.existsByUsername("admin")).thenReturn(true);

        assertThatThrownBy(() -> service.crear(u, "p"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Username ya existe");
    }
}
