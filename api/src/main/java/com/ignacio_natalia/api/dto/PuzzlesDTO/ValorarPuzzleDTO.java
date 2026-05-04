package com.ignacio_natalia.api.dto.PuzzlesDTO;

/**
 * Payload que llega desde Android cuando el usuario puntúa un puzzle.
 * POST /puzzles/valorar
 */
public class ValorarPuzzleDTO {

    private Integer idPuzzle;
    private Integer idUsuario;
    /** Puntuación 1-5 */
    private Integer valoracion;

    public Integer getIdPuzzle()    { return idPuzzle; }
    public Integer getIdUsuario()   { return idUsuario; }
    public Integer getValoracion()  { return valoracion; }

    public void setIdPuzzle(Integer idPuzzle)       { this.idPuzzle = idPuzzle; }
    public void setIdUsuario(Integer idUsuario)     { this.idUsuario = idUsuario; }
    public void setValoracion(Integer valoracion)   { this.valoracion = valoracion; }
}