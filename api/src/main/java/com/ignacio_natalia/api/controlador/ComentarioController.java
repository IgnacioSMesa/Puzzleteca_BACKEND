package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.Post.ComentarioPostDTO;
import com.ignacio_natalia.api.modelo.ComentarioPost;
import com.ignacio_natalia.api.modelo.Post;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.ComentarioPostRepository;
import com.ignacio_natalia.api.repositorio.PostRepository;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin
public class ComentarioController {

    private final ComentarioPostRepository comentarioPostRepository;
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioController(ComentarioPostRepository comentarioPostRepository,
                                PostRepository postRepository,
                                UsuarioRepository usuarioRepository) {
        this.comentarioPostRepository = comentarioPostRepository;
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * POST /comentarios/crearComentario
     * Crea un comentario en un post.
     * Body: { "contenido": "...", "idUsuario": 1, "idPost": 2 }
     */
    @PostMapping("/crearComentario")
    public ResponseEntity<ComentarioPostDTO> crear(@RequestBody ComentarioPostDTO dto) {

        try {

            Post post = postRepository.findById(dto.getIdPost())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));

            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            ComentarioPost comentario = new ComentarioPost();
            comentario.setContenido(dto.getContenido());
            comentario.setIdPost(post);
            comentario.setIdUsuario(usuario);

            ComentarioPost guardado = comentarioPostRepository.save(comentario);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ComentarioPostDTO.fromEntity(guardado));

        } catch (ResponseStatusException e) {

            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear el comentario",
                    e
            );
        }
    }

    /**
     * GET /comentarios/post/{idPost}?page=0&size=10
     * Devuelve los comentarios de un post, paginados.
     */
    @GetMapping("/post/{idPost}")
    public ResponseEntity<Page<ComentarioPostDTO>> obtenerPorPost(
            @PathVariable Integer idPost,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ComentarioPostDTO> resultado = comentarioPostRepository
                .findByIdPostIdOrderByFechaCreacionAsc(idPost, pageable)
                .map(ComentarioPostDTO::fromEntity);

        return ResponseEntity.ok(resultado);
    }

    /**
     * DELETE /comentarios/{idComentario}
     * Elimina un comentario por su id.
     */
    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idComentario) {
        if (!comentarioPostRepository.existsById(idComentario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado");
        }
        comentarioPostRepository.deleteById(idComentario);
        return ResponseEntity.noContent().build();
    }
}