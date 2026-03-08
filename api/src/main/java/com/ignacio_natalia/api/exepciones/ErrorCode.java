package com.ignacio_natalia.api.exepciones;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    INVALID_ARGUMENT("E400_01", "Argumento inválido", HttpStatus.BAD_REQUEST),

    // 404
    OBJECT_NOT_FOUND("E404_01", "Objeto no encontrado", HttpStatus.NOT_FOUND),
    DATA_EMPTY("E404_02", "No se encontraron datos", HttpStatus.NOT_FOUND),

    // 409
    DUPLICATE_ENTRY("E409_01", "Registro duplicado", HttpStatus.CONFLICT),

    //410
    INVALID_PASSWORD("E410_01", "Contraseña inválida", HttpStatus.CONFLICT),

    // 500
    DATA_ACCESS_ERROR("E500_01", "Error de acceso a datos", HttpStatus.INTERNAL_SERVER_ERROR),
    OPERATION_ERROR("E500_02", "Error interno de operación", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return code + " - " + defaultMessage;
    }
}
