package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Dependencia u oficina del municipio responsable de un conjunto de bienes.
 */
@Entity
@Table(name = "dependencia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dependencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Size(max = 250)
    @Column(length = 250)
    private String descripcion;

    @Size(max = 150)
    @Column(length = 150)
    private String ubicacion;

    @Column(nullable = false)
    private Boolean activa = Boolean.TRUE;

    @OneToMany(mappedBy = "dependencia", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Funcionario> funcionarios = new ArrayList<>();

    @OneToMany(mappedBy = "dependencia", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Bien> bienes = new ArrayList<>();
}
