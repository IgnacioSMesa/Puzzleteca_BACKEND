package com.ignacio_natalia.api.dto.UsuariosDTO;

import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;
    /** Puzzles del usuario (datos básicos, sin anidar el usuario otra vez). */
    private List<PuzzleResumen> puzzles;

    // Constructor sin puzzles (para cuando va embebido dentro de PuzzleDTO)
    public UsuarioDTO(Integer id, String nombre, String apellido,
                      String email, Usuario.TipoUsuario tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    /** Sin puzzles — usado cuando el usuario va embebido en PuzzleDTO. */
    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }

    /** Con puzzles — usado en el endpoint /listarUsuarios. */
    public static UsuarioDTO fromEntityConPuzzles(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioDTO dto = fromEntity(usuario);
        if (usuario.getPuzzles() != null) {
            dto.setPuzzles(
                    usuario.getPuzzles().stream()
                            .map(PuzzleResumen::fromEntity)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    // ── Puzzle resumido (sin anidar Usuario para evitar recursión) ────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PuzzleResumen {
        private Integer id;
        private String titulo;
        private String autor;
        private Puzzle.Dificultades dificultad;
        private Puzzle.Estados estado;
        private Integer piezas;
        private Integer tiempo;
        private Double valoracion;
        private String imagenUrl;

        public static PuzzleResumen fromEntity(Puzzle puzzle) {
            if (puzzle == null) return null;
            PuzzleResumen r = new PuzzleResumen();
            r.setId(puzzle.getId());
            r.setTitulo(puzzle.getTitulo());
            r.setAutor(puzzle.getAutor());
            r.setDificultad(puzzle.getDificultad());
            r.setEstado(puzzle.getEstado());
            r.setPiezas(puzzle.getPiezas());
            r.setTiempo(puzzle.getTiempo());
            r.setValoracion(puzzle.getValoracion_media());
            r.setImagenUrl(puzzle.getImagen());
            return r;
        }
    }
}