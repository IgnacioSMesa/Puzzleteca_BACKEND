package com.ignacio_natalia.api.exepciones;

public class OperationException extends ApiException {
    public OperationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public OperationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}