package com.ignacio_natalia.api.exepciones;

public class DataEmptyAccess extends Exception {
    public DataEmptyAccess(String message) {
        super(message);
    }
    public DataEmptyAccess(String message, Throwable cause) {
        super(message, cause);
    }
}