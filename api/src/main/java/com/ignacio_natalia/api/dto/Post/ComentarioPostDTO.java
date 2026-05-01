package com.ignacio_natalia.api.dto.Post;

import com.ignacio_natalia.api.modelo.ComentarioPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioPostDTO {

    private Integer id;
    private String contenido;
    private OffsetDateTime fechaCreacion;
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idPost;

    public static ComentarioPostDTO fromEntity(ComentarioPost comentario) {
        if (comentario == null) return null;

        ComentarioPostDTO dto = new ComentarioPostDTO();
        dto.setId(comentario.getId());
        dto.setContenido(comentario.getContenido());
        dto.setFechaCreacion(comentario.getFechaCreacion());
        dto.setIdPost(comentario.getIdPost() != null ? comentario.getIdPost().getId() : null);

        if (comentario.getIdUsuario() != null) {
            dto.setIdUsuario(comentario.getIdUsuario().getId());
            dto.setNombreUsuario(
                    comentario.getIdUsuario().getNombre() + " "
                            + comentario.getIdUsuario().getApellido());
        }
        return dto;
    }
}