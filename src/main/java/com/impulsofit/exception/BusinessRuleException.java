package com.impulsofit.exception;

/**
 * Excepción para reglas de negocio que deben reportarse como errores de validación al cliente.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException() {
        super();
    }

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

}

