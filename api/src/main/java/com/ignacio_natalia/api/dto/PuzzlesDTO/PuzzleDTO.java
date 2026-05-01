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

    /**
     * URL pública de la imagen (construida por el controlador).
     * Ej: "https://api.puzzleteca.com/imagenes/puzzles/uuid.jpg"
     * Ya NO es base64 — la BD almacena solo la ruta relativa.
     */
    private String imagenUrl;

    public static PuzzleDTO fromEntity(Puzzle puzzle, String baseImageUrl) {
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
        dto.setValoracion(puzzle.getValoracion_media());
        dto.setEstado(puzzle.getEstado());
        dto.setIdUsuario(puzzle.getIdUsuario() != null ? puzzle.getIdUsuario().getId() : null);

        // Construir URL pública si existe imagen
        if (puzzle.getImagen() != null && !puzzle.getImagen().isBlank()) {
            // Si ya es una URL completa (datos legacy base64) la dejamos tal cual;
            // si es una ruta relativa la prefijamos con la base URL.
            String rawImagen = puzzle.getImagen();
            if (rawImagen.startsWith("http") || rawImagen.startsWith("data:")) {
                dto.setImagenUrl(rawImagen);
            } else {
                dto.setImagenUrl(baseImageUrl + rawImagen);
            }
        }

        return dto;
    }

    /** @deprecated usa {@link #fromEntity(Puzzle, String)} */
    @Deprecated
    public static PuzzleDTO fromEntity(Puzzle puzzle) {
        return fromEntity(puzzle, "");
    }

    public Puzzle toEntity() {
        Puzzle puzzle = new Puzzle();
        puzzle.setId(this.id);
        puzzle.setTitulo(this.titulo);
        puzzle.setAutor(this.autor);
        puzzle.setTiempo(this.tiempo);
        puzzle.setPiezas(this.piezas);
        puzzle.setDificultad(this.dificultad);
        puzzle.setDescripcion(this.descripcion);
        puzzle.setColor(this.color != null && this.color);
        puzzle.setValoracion_media(this.valoracion != null ? this.valoracion : 0);
        puzzle.setEstado(this.estado);
        // imagenUrl ya es la ruta relativa tal como la devuelve ImagenService
        puzzle.setImagen(this.imagenUrl);

        if (this.idUsuario != null) {
            com.ignacio_natalia.api.modelo.Usuario u = new com.ignacio_natalia.api.modelo.Usuario();
            u.setId(this.idUsuario);
            puzzle.setIdUsuario(u);
        }
        return puzzle;
    }
}
