package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Bien mueble del inventario municipal. Entidad central del modelo.
 */
@Entity
@Table(name = "bien", indexes = {
        @Index(name = "idx_bien_placa", columnList = "placa", unique = true),
        @Index(name = "idx_bien_estado", columnList = "estado")
})
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bien {

    public enum Estado { DISPONIBLE, ASIGNADO, EN_MANTENIMIENTO, BAJA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25, unique = true)
    private String placa;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;

    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;

    @Size(max = 60)
    @Column(length = 60)
    private String marca;

    @Size(max = 60)
    @Column(length = 60)
    private String modelo;

    @Size(max = 60)
    @Column(length = 60)
    private String serial;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "valor_compra", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorCompra;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependencia_id", nullable = false)
    private Dependencia dependencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @CreatedDate
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
