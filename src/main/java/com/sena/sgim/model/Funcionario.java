package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Funcionario del municipio al que se le pueden asignar bienes.
 */
@Entity
@Table(name = "funcionario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String apellidos;

    @Size(max = 80)
    @Column(length = 80)
    private String cargo;

    @Email
    @Size(max = 120)
    @Column(length = 120)
    private String correo;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependencia_id", nullable = false)
    private Dependencia dependencia;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;

    @Transient
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
