package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.modelo.Comentario;
import com.ignacio_natalia.api.repositorio.ComentarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository repository;

    public ComentarioService(ComentarioRepository repository) {
        this.repository = repository;
    }

    public Comentario crearComentario(Comentario comentario) {
        return repository.save(comentario);
    }

    public List<Comentario> obtenerPorPuzzle(Integer idPuzzle) {
        return repository.findByIdPuzzleOrderByCreadoEnDesc(idPuzzle);
    }
}
