package com.impulsofit.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException {

    private final HttpStatus status;

    public BusinessRuleException() {
        super();
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BusinessRuleException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BusinessRuleException(String message, HttpStatus status) {
        super(message);
        if (status == null) {
            this.status = HttpStatus.BAD_REQUEST;
        } else {
            this.status = status;
        }
    }

    public BusinessRuleException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        if (status == null) {
            this.status = HttpStatus.BAD_REQUEST;
        } else {
            this.status = status;
        }
    }

    public HttpStatus getStatus() {
        return status;
    }
}
