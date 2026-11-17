package com.sena.sgim.service;

import com.sena.sgim.exception.ReglaNegocioException;
import com.sena.sgim.model.Bien;
import com.sena.sgim.model.Categoria;
import com.sena.sgim.model.Dependencia;
import com.sena.sgim.model.Funcionario;
import com.sena.sgim.repository.BienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la logica de negocio de BienService.
 */
@ExtendWith(MockitoExtension.class)
class BienServiceTest {

    @Mock private BienRepository bienRepository;
    @Mock private CategoriaService categoriaService;
    @Mock private ProveedorService proveedorService;
    @Mock private DependenciaService dependenciaService;
    @Mock private FuncionarioService funcionarioService;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks private BienService service;

    private Bien bien;
    private Categoria categoria;
    private Dependencia dependencia;

    @BeforeEach
    void setUp() {
        categoria   = Categoria.builder().id(1L).nombre("Equipos").activa(true).build();
        dependencia = Dependencia.builder().id(1L).nombre("Alcaldia").activa(true).build();
        bien = Bien.builder().id(10L).placa("P-100").nombre("Laptop")
                .valorCompra(new BigDecimal("3500000"))
                .estado(Bien.Estado.DISPONIBLE)
                .categoria(categoria).dependencia(dependencia).build();
    }

    @Test
    void crear_falla_cuando_placa_duplicada() {
        when(bienRepository.existsByPlaca("P-100")).thenReturn(true);

        assertThatThrownBy(() -> service.crear(bien, 1L, null, 1L, "tester"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Placa ya registrada");

        verify(bienRepository, never()).save(any());
    }

    @Test
    void crear_persiste_bien_cuando_datos_validos() {
        when(bienRepository.existsByPlaca("P-100")).thenReturn(false);
        when(categoriaService.obtener(1L)).thenReturn(categoria);
        when(dependenciaService.obtener(1L)).thenReturn(dependencia);
        when(bienRepository.save(any(Bien.class))).thenAnswer(inv -> inv.getArgument(0));

        Bien guardado = service.crear(bien, 1L, null, 1L, "tester");

        assertThat(guardado.getEstado()).isEqualTo(Bien.Estado.DISPONIBLE);
        assertThat(guardado.getCategoria()).isSameAs(categoria);
        verify(bienRepository).save(any(Bien.class));
    }

    @Test
    void asignar_falla_si_bien_dado_de_baja() {
        bien.setEstado(Bien.Estado.BAJA);
        when(bienRepository.findById(10L)).thenReturn(Optional.of(bien));

        assertThatThrownBy(() -> service.asignar(10L, 5L, "tester"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("dado de baja");
    }

    @Test
    void asignar_cambia_estado_a_asignado() {
        Funcionario f = Funcionario.builder().id(5L).documento("123").nombres("Ana").apellidos("Diaz").build();
        when(bienRepository.findById(10L)).thenReturn(Optional.of(bien));
        when(funcionarioService.obtener(5L)).thenReturn(f);

        Bien resultado = service.asignar(10L, 5L, "tester");

        assertThat(resultado.getEstado()).isEqualTo(Bien.Estado.ASIGNADO);
        assertThat(resultado.getFuncionario()).isSameAs(f);
    }

    @Test
    void liberar_deja_bien_disponible() {
        bien.setEstado(Bien.Estado.ASIGNADO);
        when(bienRepository.findById(10L)).thenReturn(Optional.of(bien));

        Bien resultado = service.liberar(10L, "tester");

        assertThat(resultado.getFuncionario()).isNull();
        assertThat(resultado.getEstado()).isEqualTo(Bien.Estado.DISPONIBLE);
    }
}
