package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.ComentarioPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioPostRepository extends JpaRepository<ComentarioPost, Integer> {

    /** Comentarios de un post, paginados y ordenados por fecha */
    Page<ComentarioPost> findByIdPostIdOrderByFechaCreacionAsc(Integer idPost, Pageable pageable);
}