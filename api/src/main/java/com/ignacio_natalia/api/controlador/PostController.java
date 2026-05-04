package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.Post.PostDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Post;
import com.ignacio_natalia.api.repositorio.ErrorResponse;
import com.ignacio_natalia.api.repositorio.PostRepository;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Controlador REST para el foro de Posts.
 *
 * Rutas:
 *  POST   /posts/crearPost          — crea un post (multipart/form-data)
 *  GET    /posts/feed               — feed paginado
 *  DELETE /posts/eliminar/{idPost}  — elimina un post del usuario
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final InterfazDAO dao;

    /**
     * Base URL que se antepone a imagen_url para construir la URL pública.
     * Configurable en application.properties:
     *   app.image.base-url=https://api.puzzleteca.com/imagenes/
     */
    @Value("${app.image.base-url:http://localhost:8080/imagenes/}")
    private String imageBaseUrl;

    private final PostRepository postRepository;

    public PostController(InterfazDAO dao, PostRepository postRepository) {
        this.dao = dao;
        this.postRepository = postRepository;
    }

    // -------------------------------------------------------------------------
    // POST /posts/crearPost
    // -------------------------------------------------------------------------

    /**
     * Crea un nuevo post con imagen opcional.
     *
     * Recibe multipart/form-data:
     *  - idUsuario  (Long, requerido)
     *  - contenido  (String, opcional si hay imagen)
     *  - imagen     (MultipartFile, opcional si hay contenido)
     */
    @PostMapping(value = "/crearPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPost(
            @RequestParam Integer idUsuario,
            @RequestParam(required = false) String contenido,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            Post post = dao.crearPost(idUsuario, contenido, imagen);
            PostDTO dto = PostDTO.fromEntity(post, imageBaseUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);

        } catch (ArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Datos inválidos: se requiere contenido o imagen", 400));

        } catch (IllegalArgumentException e) {
            // Lanzado por ImagenService al recibir imagen inválida
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), 400));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al procesar la imagen", 500));

        } catch (DataBaseAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error de acceso a base de datos", 500));

        } catch (OperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al crear el post", 500));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error inesperado", 500));
        }
    }

    // -------------------------------------------------------------------------
    // GET /posts/feed
    // -------------------------------------------------------------------------

    /**
     * Feed paginado de posts, más recientes primero.
     *
     * @param pagina  página 0-indexed (por defecto 0)
     * @param tamanno posts por página (por defecto 20, máx 50)
     */
    @GetMapping("/feed")
    public ResponseEntity<?> feed(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanno) {

        try {
            Page<Post> posts = dao.listarPosts(pagina, tamanno);

            Page<PostDTO> dtos = posts.map(p -> PostDTO.fromEntity(p, imageBaseUrl));
            return ResponseEntity.ok(dtos);

        } catch (DataEmptyAccess e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay posts disponibles", 404));

        } catch (DataBaseAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error de acceso a base de datos", 500));
        }
    }

    // -------------------------------------------------------------------------
    // DELETE /posts/eliminar/{idPost}
    // -------------------------------------------------------------------------

    @DeleteMapping("/eliminar/{idPost}")
    public ResponseEntity<?> eliminarPost(
            @PathVariable Integer idPost,
            @RequestParam Integer idUsuario) {

        try {
            dao.eliminarPost(idPost, idUsuario);
            return ResponseEntity.ok(Map.of("mensaje", "Post eliminado correctamente"));

        } catch (ArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Datos inválidos", 400));

        } catch (ObjectNotExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Post no encontrado o no pertenece al usuario", 404));

        } catch (DataBaseAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error de acceso a base de datos", 500));
        }
    }

    @PostMapping("/{idPost}/like")
    @Transactional
    public ResponseEntity<?> toggleLike(@PathVariable Integer idPost,
                                        @RequestParam boolean liked) {
        try {
            if (liked) {
                postRepository.incrementarLikes(idPost);
            } else {
                postRepository.decrementarLikes(idPost);
            }
            return ResponseEntity.ok(Map.of("liked", liked));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar likes", 500));
        }
    }

}