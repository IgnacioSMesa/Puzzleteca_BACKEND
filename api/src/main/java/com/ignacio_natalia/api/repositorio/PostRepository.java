package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {

    /** Feed paginado — todos los posts ordenados por más recientes */
    Page<Post> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    /** Posts de un usuario concreto, paginados */
    Page<Post> findByIdUsuarioIdOrderByFechaCreacionDesc(
            @Param("idUsuario") Integer idUsuario, Pageable pageable);

    /** Incrementa likes atómicamente (evita race conditions) */
    @Modifying
    @Query("UPDATE Post p SET p.totalLikes = p.totalLikes + 1 WHERE p.id = :idPost")
    int incrementarLikes(@Param("idPost") Integer idPost);

    /** Decrementa likes — no baja de 0 gracias al CHECK de la BD */
    @Modifying
    @Query("UPDATE Post p SET p.totalLikes = GREATEST(p.totalLikes - 1, 0) WHERE p.id = :idPost")
    int decrementarLikes(@Param("idPost") Integer idPost);

    /** Incrementa el contador de comentarios atómicamente */
    @Modifying
    @Query("UPDATE Post p SET p.totalComentarios = p.totalComentarios + 1 WHERE p.id = :idPost")
    int incrementarComentarios(@Param("idPost") Integer idPost);

}
