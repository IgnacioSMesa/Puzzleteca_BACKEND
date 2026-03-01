package com.ignacio_natalia.api.exepciones;

public class ObjectNotExist extends ApiException {
    public ObjectNotExist(ErrorCode errorCode) {
        super(errorCode);
    }
    public ObjectNotExist(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}