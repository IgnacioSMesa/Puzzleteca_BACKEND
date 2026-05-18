package com.ignacio_natalia.api.dto.PuzzlesDTO;

import com.ignacio_natalia.api.dto.UsuariosDTO.UsuarioDTO;
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
    private Double valoracion;
    private Puzzle.Estados estado;
    /** Usuario propietario del puzzle (id, nombre, apellido, email, tipoUsuario). */
    private UsuarioDTO usuario;

    /**
     * URL pública de la imagen (construida por el controlador).
     * Ya NO es base64 — la BD almacena solo la ruta relativa.
     */
    private String imagenUrl;

    /** True si el usuario autenticado ya valoró este puzzle. */
    private boolean yaValoradoPorUsuario;

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
        dto.setUsuario(UsuarioDTO.fromEntity(puzzle.getIdUsuario()));

        if (puzzle.getImagen() != null && !puzzle.getImagen().isBlank()) {
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
        puzzle.setValoracion_media(this.valoracion != null ? this.valoracion : 0.0);
        puzzle.setEstado(this.estado);
        puzzle.setImagen(this.imagenUrl != null ? this.imagenUrl : "puzzles/foto_predeterminada.png");

        if (this.usuario != null && this.usuario.getId() != null) {
            com.ignacio_natalia.api.modelo.Usuario u = new com.ignacio_natalia.api.modelo.Usuario();
            u.setId(this.usuario.getId());
            puzzle.setIdUsuario(u);
        }
        return puzzle;
    }
}