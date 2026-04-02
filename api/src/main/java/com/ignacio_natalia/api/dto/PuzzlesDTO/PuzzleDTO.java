package com.ignacio_natalia.api.dto.PuzzlesDTO;

import com.ignacio_natalia.api.modelo.Puzzle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleDTO {

    private Integer id;
    private String autor;
    private Integer tiempo;
    private Integer piezas;
    private Puzzle.Dificultades dificultad;
    private String descripcion;
    private Boolean color;
    private Integer valoracion;
    private Puzzle.Estados estado;
    private Integer idUsuario;

    public static PuzzleDTO fromEntity(Puzzle puzzle) {
        if (puzzle == null) return null;
        Integer usuarioId = (puzzle.getIdUsuario() != null) ? puzzle.getIdUsuario().getId() : null;
        PuzzleDTO dto = new PuzzleDTO();
        dto.setId(puzzle.getId());
        dto.setAutor(puzzle.getAutor());
        dto.setTiempo(puzzle.getTiempo());
        dto.setPiezas(puzzle.getPiezas());
        dto.setDificultad(puzzle.getDificultad());
        dto.setDescripcion(puzzle.getDescripcion());
        dto.setColor(puzzle.isColor());
        dto.setValoracion(puzzle.getValoracion());
        dto.setEstado(puzzle.getEstado());
        dto.setIdUsuario(usuarioId);
        return dto;
    }

}