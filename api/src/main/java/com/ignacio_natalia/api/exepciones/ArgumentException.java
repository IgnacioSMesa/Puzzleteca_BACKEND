package com.ignacio_natalia.api.exepciones;

public class ArgumentException extends ApiException {
    public ArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }
}