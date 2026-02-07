package com.ignacio_natalia.api.exepciones;

public class ObjectNotExist extends Exception {
    public ObjectNotExist(String message) {
        super(message);
    }
    public ObjectNotExist(String message, Throwable cause) {
        super(message, cause);
    }
}