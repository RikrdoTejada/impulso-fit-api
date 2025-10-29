package com.impulsofit.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException {

    private final HttpStatus status;

    public BusinessRuleException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    // Getter para HttpStatus
    public HttpStatus getStatus() {
        return status;
    }

    // Getter para el mensaje
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
