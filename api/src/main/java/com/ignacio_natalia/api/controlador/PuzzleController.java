package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.PuzzleDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.servicios.InterfazDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/puzzles")
public class PuzzleController {

    private final InterfazDAO dao;

    public PuzzleController(InterfazDAO dao) {
        this.dao = dao;
    }

    // POST /api/puzzles
    @PostMapping
    public ResponseEntity<Void> crearPuzzle(@RequestBody Puzzle puzzle) throws ArgumentException, DataBaseAccessException, OperationException {
        dao.insertarPuzzle(puzzle);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/puzzles
    @GetMapping
    public ResponseEntity<List<PuzzleDTO>> listarPuzzles() throws DataEmptyAccess, ArgumentException, DataBaseAccessException {

        List<Puzzle> puzzles = dao.listarPuzzles();
        List<PuzzleDTO> puzzleDTOS = new ArrayList<>();

        for (Puzzle puzzle : puzzles) {
            puzzleDTOS.add(PuzzleDTO.fromEntity(puzzle));
        }

        return ResponseEntity.ok(puzzleDTOS);
    }

}
