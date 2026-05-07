package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.modelo.ComentarioPost;
import com.ignacio_natalia.api.repositorio.ComentarioPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioPostRepository repository;

    public ComentarioService(ComentarioPostRepository repository) {
        this.repository = repository;
    }

    public ComentarioPost crearComentario(ComentarioPost comentario) {
        return repository.save(comentario);
    }

    public Page<ComentarioPost> obtenerPorPost(Integer idPost, Pageable pageable) {
        return repository.findByIdPostIdOrderByFechaCreacionAsc(idPost, pageable);
    }
}
