package com.ignacio_natalia.api.exepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /* =====================================
       MANEJO DE TODAS LAS ApiException
       ===================================== */

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {

        ErrorCode errorCode = ex.getErrorCode();

        logger.warn("Error controlado [{}]: {}",
                errorCode.getCode(),
                errorCode.getDefaultMessage()
        );

        return buildResponse(
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                errorCode.getDefaultMessage()
        );
    }

    /* =====================================
       FALLBACK (cualquier otra excepción)
       ===================================== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {

        logger.error("Error inesperado en el servidor", ex);

        return buildResponse(
                ErrorCode.OPERATION_ERROR.getHttpStatus(),
                "E500_00",
                "Error inesperado en el servidor"
        );
    }

    /* =====================================
       MÉTODO AUXILIAR
       ===================================== */

    private ResponseEntity<Object> buildResponse(
            org.springframework.http.HttpStatus status,
            String code,
            String message
    ) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("code", code);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}