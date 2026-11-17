package com.sena.sgim.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registro de auditoria para operaciones sensibles del sistema.
 */
@Entity
@Table(name = "auditoria", indexes = {
        @Index(name = "idx_audit_fecha", columnList = "fecha"),
        @Index(name = "idx_audit_usuario", columnList = "usuario")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Auditoria {

    public enum Accion { CREAR, ACTUALIZAR, ELIMINAR, LOGIN, LOGOUT, ERROR }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Accion accion;

    @Column(nullable = false, length = 60)
    private String usuario;

    @Column(nullable = false, length = 60)
    private String entidad;

    @Column(name = "entidad_id", length = 40)
    private String entidadId;

    @Column(length = 500)
    private String detalle;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
