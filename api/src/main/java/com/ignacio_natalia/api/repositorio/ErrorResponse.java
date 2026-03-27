package com.ignacio_natalia.api.repositorio;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String mensaje;
    private int codigo;

    public ErrorResponse(String mensaje, int codigo) {
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

}