package com.sena.sgim.exception;

/** Lanzada cuando se viola una regla de negocio (ej: placa duplicada). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
