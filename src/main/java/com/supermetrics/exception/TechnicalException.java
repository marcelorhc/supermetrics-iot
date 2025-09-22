package com.supermetrics.exception;

public class TechnicalException extends RuntimeException {
    public TechnicalException(String message, Exception e) {
        super(message, e);
    }
}
