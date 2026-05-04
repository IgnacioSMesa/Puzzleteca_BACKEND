package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.PuzzlesDTO.ValorarPuzzleDTO;
import com.ignacio_natalia.api.dto.RankingDTO.RankingDiarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.servicios.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * GET /ranking/diario
     * 200 → lista ordenada por media DESC
     * 204 → nadie tiene valoraciones hoy (Android muestra estado vacío)
     * 5xx → gestionado automáticamente por GlobalExceptionHandler
     */
    @GetMapping("/diario")
    public ResponseEntity<?> rankingDiario() throws DataBaseAccessException {
        List<RankingDiarioDTO> ranking = rankingService.obtenerRankingDiario();
        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(ranking);
    }

    /**
     * POST /ranking/valorar
     * Body JSON: { "idPuzzle": 1, "idUsuario": 2, "valoracion": 4 }
     *
     * 201 → valoración guardada
     * 400 / 404 / 409 / 500 → gestionados por GlobalExceptionHandler según ErrorCode
     */
    @PostMapping("/valorar")
    public ResponseEntity<?> valorarPuzzle(@RequestBody ValorarPuzzleDTO dto)
            throws ArgumentException, ObjectNotExist, DuplicateEntry, DataBaseAccessException {
        rankingService.valorarPuzzle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Valoración registrada");
    }
}