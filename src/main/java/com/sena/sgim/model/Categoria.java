package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Categoria de clasificacion de los bienes (ej: equipos de computo, mobiliario, vehiculos).
 */
@Entity
@Table(name = "categoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80, unique = true)
    private String nombre;

    @Size(max = 250)
    @Column(length = 250)
    private String descripcion;

    @Column(name = "vida_util_meses")
    private Integer vidaUtilMeses;

    @Column(nullable = false)
    private Boolean activa = Boolean.TRUE;
}
