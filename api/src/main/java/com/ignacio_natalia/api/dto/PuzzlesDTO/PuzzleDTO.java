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
    private String titulo;
    private String autor;
    private Integer tiempo;
    private Integer piezas;
    private Puzzle.Dificultades dificultad;
    private String descripcion;
    private Boolean color;
    private Integer valoracion;
    private Puzzle.Estados estado;
    private Integer idUsuario;
    private String imagenBase64;

    public static PuzzleDTO fromEntity(Puzzle puzzle) {
        if (puzzle == null) return null;
        PuzzleDTO dto = new PuzzleDTO();
        dto.setId(puzzle.getId());
        dto.setTitulo(puzzle.getTitulo());
        dto.setAutor(puzzle.getAutor());
        dto.setTiempo(puzzle.getTiempo());
        dto.setPiezas(puzzle.getPiezas());
        dto.setDificultad(puzzle.getDificultad());
        dto.setDescripcion(puzzle.getDescripcion());
        dto.setColor(puzzle.isColor());
        dto.setValoracion(puzzle.getValoracion());
        dto.setEstado(puzzle.getEstado());
        dto.setIdUsuario(puzzle.getIdUsuario() != null ? puzzle.getIdUsuario().getId() : null);
        dto.setImagenBase64(puzzle.getImagen());
        return dto;
    }

    public Puzzle toEntity() {
        Puzzle puzzle = new Puzzle();
        puzzle.setTitulo(this.titulo);
        puzzle.setAutor(this.autor);
        puzzle.setTiempo(this.tiempo);
        puzzle.setPiezas(this.piezas);
        puzzle.setDificultad(this.dificultad);
        puzzle.setDescripcion(this.descripcion);
        puzzle.setColor(this.color != null && this.color);
        puzzle.setValoracion(this.valoracion != null ? this.valoracion : 0);
        puzzle.setEstado(this.estado);
        puzzle.setImagen(this.imagenBase64);

        if (this.idUsuario != null) {
            com.ignacio_natalia.api.modelo.Usuario u = new com.ignacio_natalia.api.modelo.Usuario();
            u.setId(this.idUsuario);
            puzzle.setIdUsuario(u);
        }
        return puzzle;
    }
}