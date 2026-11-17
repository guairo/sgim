package com.sena.sgim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Usuario del sistema con credenciales de acceso y rol.
 */
@Entity
@Table(name = "usuario_sistema")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioSistema {

    public enum Rol { ADMIN, OPERADOR, CONSULTA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Email
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120, unique = true)
    private String correo;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol = Rol.CONSULTA;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
}
