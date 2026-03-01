package com.ignacio_natalia.api.exepciones;

public class DuplicateEntry extends ApiException {
    public DuplicateEntry(ErrorCode errorCode) {
        super(errorCode);
    }
    public DuplicateEntry(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}