package com.ignacio_natalia.api.exepciones;

public class DuplicateEntry extends Exception {
    public DuplicateEntry(String message) {
        super(message);
    }
    public DuplicateEntry(String message, Throwable cause) {
        super(message, cause);
    }
}