package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.exepciones.ArgumentException;
import com.ignacio_natalia.api.exepciones.DataEmptyAccess;
import com.ignacio_natalia.api.exepciones.ObjectNotExist;
import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.servicios.InterfazDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puzzles")
public class ControladorPuzzle {

    private final InterfazDAO dao;

    public ControladorPuzzle(InterfazDAO dao) {
        this.dao = dao;
    }

    // POST /api/puzzles
    @PostMapping
    public ResponseEntity<Void> crearPuzzle(@RequestBody Puzzle puzzle) throws ArgumentException {
        dao.insertarPuzzle(puzzle);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/puzzles
    @GetMapping
    public ResponseEntity<List<Puzzle>> listarPuzzles() throws DataEmptyAccess, ArgumentException {
        return ResponseEntity.ok(dao.listarPuzzles());
    }

    // PATCH /api/puzzles/{idPuzzle}/usuario/{idUsuario}
    @PatchMapping("/{idPuzzle}/usuario/{idUsuario}")
    public ResponseEntity<Void> actualizarPuzzle(
            @PathVariable Integer idPuzzle,
            @PathVariable Integer idUsuario,
            @RequestBody Map<String, String> body) throws ObjectNotExist, DataEmptyAccess, ArgumentException {

        dao.actualizarPuzzle(
                idUsuario,
                idPuzzle,
                body.get("atributo"),
                body.get("valor")
        );
        return ResponseEntity.ok().build();
    }

    // GET /api/puzzles/top
    @GetMapping("/top")
    public ResponseEntity<Puzzle[]> topCinco() throws DataEmptyAccess, ArgumentException {
        return ResponseEntity.ok(dao.topCinco());
    }

    // GET /api/puzzles/mejor-tiempo
    @GetMapping("/mejor-tiempo")
    public ResponseEntity<Integer> mejorTiempo() throws DataEmptyAccess {
        return ResponseEntity.ok(dao.mejorTiempo());
    }
}
