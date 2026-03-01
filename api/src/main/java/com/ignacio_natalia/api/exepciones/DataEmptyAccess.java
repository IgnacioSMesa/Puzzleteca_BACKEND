package com.ignacio_natalia.api.exepciones;

public class DataEmptyAccess extends ApiException {
    public DataEmptyAccess(ErrorCode errorCode) {
        super(errorCode);
    }
    public DataEmptyAccess(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}