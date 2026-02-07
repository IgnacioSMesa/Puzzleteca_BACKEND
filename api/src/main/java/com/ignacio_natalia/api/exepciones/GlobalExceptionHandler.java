package com.ignacio_natalia.api.exepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* =========================
       400 - BAD REQUEST
       ========================= */

    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<Object> handleArgumentException(ArgumentException ex) {
        logger.warn("Error de argumentos: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /* =========================
       404 - NOT FOUND
       ========================= */

    @ExceptionHandler(ObjectNotExist.class)
    public ResponseEntity<Object> handleObjectNotExist(ObjectNotExist ex) {
        logger.warn("Objeto no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DataEmptyAccess.class)
    public ResponseEntity<Object> handleDataEmpty(DataEmptyAccess ex) {
        logger.warn("Datos vacíos: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /* =========================
       409 - CONFLICT
       ========================= */

    @ExceptionHandler(DuplicateEntry.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateEntry ex) {
        logger.warn("Entrada duplicada: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* =========================
       500 - INTERNAL SERVER ERROR
       ========================= */

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccess(DataAccessException ex) {
        logger.error("Error de acceso a datos", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<Object> handleOperation(OperationException ex) {
        logger.error("Error de operación", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /* =========================
       FALLBACK (cualquier otra)
       ========================= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        logger.error("Error inesperado en el servidor" + ex.getMessage());
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error inesperado en el servidor"
        );
    }

    /* =========================
       MÉTODO AUXILIAR
       ========================= */

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}