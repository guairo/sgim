package com.sena.sgim.exception;

/** Lanzada cuando un recurso referido por id no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
