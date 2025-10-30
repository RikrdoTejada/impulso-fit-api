package com.impulsofit.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException {

    private final HttpStatus status;

    public BusinessRuleException(String message) {
        super(message);
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


    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}