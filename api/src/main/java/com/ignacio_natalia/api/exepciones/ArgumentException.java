package com.ignacio_natalia.api.exepciones;

public class ArgumentException extends Exception {
    public ArgumentException(String message) {
        super(message);
    }
    public ArgumentException(String message, Throwable cause) {super(message, cause);}
}