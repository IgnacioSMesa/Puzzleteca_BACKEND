package com.ignacio_natalia.api.dto.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para crear un Post.
 *
 * Se recibe como {@code multipart/form-data} desde el controlador porque
 * incluye un campo binario (imagen). Los campos de texto llegan como
 * {@code @RequestParam} y la imagen como {@code @RequestPart MultipartFile}.
 */
@Getter
@Setter
@NoArgsConstructor
public class CrearPostDTO {

    /** Texto del post — puede ser null si sólo se sube imagen */
    private String contenido;

    /** ID del usuario que crea el post (obtenido del JWT en el controlador) */
    private Integer idUsuario;
}