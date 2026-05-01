package com.ignacio_natalia.api.dto.Post;

import com.ignacio_natalia.api.modelo.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO de respuesta para un Post.
 * Devuelve la URL pública de la imagen (no base64) para que el cliente
 * la cargue directamente desde el servidor estático o CDN.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;
    private String contenido;

    /**
     * URL pública accesible desde el frontend.
     * Ej: "https://api.puzzleteca.com/imagenes/posts/uuid.jpg"
     * El controlador la construye concatenando la base URL + imagenUrl de la BD.
     */
    private String imagenUrl;

    private OffsetDateTime fechaCreacion;
    private Integer totalLikes;
    private Integer totalComentarios;

    /** Info básica del autor — evitamos exponer la entidad completa */
    private Integer idUsuario;
    private String nombreUsuario;

    // -------------------------------------------------------------------------

    public static PostDTO fromEntity(Post post, String baseImageUrl) {
        if (post == null) return null;

        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContenido(post.getContenido());
        dto.setFechaCreacion(post.getFechaCreacion());
        dto.setTotalLikes(post.getTotalLikes());
        dto.setTotalComentarios(post.getTotalComentarios());

        if (post.getIdUsuario() != null) {
            dto.setIdUsuario(post.getIdUsuario().getId());
            dto.setNombreUsuario(
                    post.getIdUsuario().getNombre() + " " + post.getIdUsuario().getApellido());
        }

        // Construir URL pública sólo si existe imagen
        if (post.getImagenUrl() != null && !post.getImagenUrl().isBlank()) {
            // baseImageUrl ej: "https://api.puzzleteca.com/imagenes/"
            dto.setImagenUrl(baseImageUrl + post.getImagenUrl());
        }

        return dto;
    }
}