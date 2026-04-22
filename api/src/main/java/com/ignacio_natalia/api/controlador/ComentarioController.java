package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.ComentarioDTO;
import com.ignacio_natalia.api.modelo.Comentario;
import com.ignacio_natalia.api.servicios.ComentarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin
public class ComentarioController {

    private final ComentarioService service;

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    @PostMapping("/crearComentario")
    public Comentario crear(@RequestBody ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setContenido(comentarioDTO.getContenido());
        comentario.setIdUsuario(comentarioDTO.getIdUsuario());
        comentario.setIdPuzzle(comentarioDTO.getIdPuzzle());

        return service.crearComentario(comentario);
    }

    @GetMapping("/puzzle/{idPuzzle}")
    public List<ComentarioDTO> obtenerPorPuzzle(@PathVariable Integer idPuzzle) {
        return service.obtenerPorPuzzle(idPuzzle)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ComentarioDTO convertirADTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getContenido(),
                comentario.getIdUsuario(),
                comentario.getIdPuzzle()
        );
    }
}