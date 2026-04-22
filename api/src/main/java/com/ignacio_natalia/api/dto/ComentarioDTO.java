package com.ignacio_natalia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {

    private String contenido;
    private Integer idUsuario;
    private Integer idPuzzle;

}