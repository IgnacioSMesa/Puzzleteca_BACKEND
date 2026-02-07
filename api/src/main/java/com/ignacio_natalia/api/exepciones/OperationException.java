package com.ignacio_natalia.api.exepciones;

public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }
    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
