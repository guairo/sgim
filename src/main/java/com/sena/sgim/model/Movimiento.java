package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Movimiento historico de un bien: entrada, asignacion, traslado, baja, devolucion.
 */
@Entity
@Table(name = "movimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movimiento {

    public enum Tipo { ENTRADA, ASIGNACION, TRASLADO, DEVOLUCION, BAJA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tipo tipo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id", nullable = false)
    private Bien bien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependencia_origen_id")
    private Dependencia dependenciaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependencia_destino_id")
    private Dependencia dependenciaDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_origen_id")
    private Funcionario funcionarioOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_destino_id")
    private Funcionario funcionarioDestino;

    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Size(max = 60)
    @Column(name = "usuario_registro", length = 60)
    private String usuarioRegistro;
}
