package com.ignacio_natalia.api.dto.PuzzlesDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPuzzleDTO {
    private Integer idUsuario;
    private Integer idPuzzle;
    private String atributo;
    private String cambio;
}