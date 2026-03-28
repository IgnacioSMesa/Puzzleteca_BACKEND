package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.PuzzlesDTO.ActualizarPuzzleDTO;
import com.ignacio_natalia.api.dto.PuzzlesDTO.PuzzleDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.repositorio.ErrorResponse;
import com.ignacio_natalia.api.servicios.InterfazDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/puzzles")
public class PuzzleController {

    private final InterfazDAO dao;

    public PuzzleController(InterfazDAO dao) {
        this.dao = dao;
    }

    // POST /api/puzzles
    @PostMapping("/registrarPuzzle")
    public ResponseEntity<Void> crearPuzzle(@RequestBody Puzzle puzzle) throws ArgumentException, DataBaseAccessException, OperationException {
        dao.insertarPuzzle(puzzle);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/puzzles
    @GetMapping("/obtenerPuzzles")
    public ResponseEntity<?> listarPuzzles() {

        try {
            List<Puzzle> puzzles = dao.listarPuzzles();

            List<PuzzleDTO> puzzleDTOS = puzzles.stream()
                    .map(PuzzleDTO::fromEntity)
                    .toList();

            return ResponseEntity.ok(puzzleDTOS);

        } catch (DataBaseAccessException db) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 409));

        } catch (DataEmptyAccess e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay puzzles registrados", 404));
        }
    }

    // Actualizar puzzle
    @PostMapping("/actualizarPuzzle")
    public ResponseEntity<?> actualizarPuzzle(@RequestBody ActualizarPuzzleDTO dto) {

        try {

            dao.actualizarPuzzle(dto.getIdUsuario(), dto.getIdPuzzle(), dto.getAtributo(), dto.getCambio());
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (ObjectNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("El puzzle no existe o no pertenece al usuario", 404));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El puzzle ya tiene ese valor, no hay nada que actualizar", 409));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));

        } catch (OperationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar el puzzle", 500));
        }
    }

}
