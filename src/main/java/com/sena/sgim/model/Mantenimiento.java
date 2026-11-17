package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mantenimiento preventivo o correctivo realizado a un bien.
 */
@Entity
@Table(name = "mantenimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Mantenimiento {

    public enum Tipo { PREVENTIVO, CORRECTIVO }
    public enum Estado { PROGRAMADO, EN_EJECUCION, FINALIZADO, CANCELADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id", nullable = false)
    private Bien bien;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tipo tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.PROGRAMADO;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "fecha_ejecucion")
    private LocalDate fechaEjecucion;

    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;

    @Column(precision = 15, scale = 2)
    private BigDecimal costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;
}
