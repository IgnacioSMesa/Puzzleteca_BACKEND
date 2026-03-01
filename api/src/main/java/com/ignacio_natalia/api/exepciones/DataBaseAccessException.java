package com.ignacio_natalia.api.exepciones;

public class DataBaseAccessException extends ApiException {
    public DataBaseAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
    public DataBaseAccessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}