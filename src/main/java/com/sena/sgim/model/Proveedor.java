package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Proveedor del cual se adquieren los bienes.
 */
@Entity
@Table(name = "proveedor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20, unique = true)
    private String nit;

    @NotBlank
    @Size(max = 150)
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @Email
    @Size(max = 120)
    @Column(length = 120)
    private String correo;

    @Size(max = 200)
    @Column(length = 200)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;
}
