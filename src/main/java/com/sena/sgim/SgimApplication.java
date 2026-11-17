package com.sena.sgim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Clase principal del Sistema de Gestion para Inventario Municipal (SGIM).
 * Proyecto formativo SENA ADSO - Aplicacion web Spring Boot.
 */
@SpringBootApplication
@EnableJpaAuditing
public class SgimApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgimApplication.class, args);
    }
}
