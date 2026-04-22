package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByIdPuzzleOrderByCreadoEnDesc(Integer idPuzzle);
}