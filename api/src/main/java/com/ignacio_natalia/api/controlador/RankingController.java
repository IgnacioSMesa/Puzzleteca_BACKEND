package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.config.JwtUtil;
import com.ignacio_natalia.api.dto.PuzzlesDTO.ValorarPuzzleDTO;
import com.ignacio_natalia.api.dto.RankingDTO.RankingDiarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.servicios.RankingService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
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
     * Header: Authorization: Bearer <token>
     * Body JSON: { "idPuzzle": 1, "valoracion": 4 }
     *
     * El idUsuario se extrae del token JWT para evitar que el cliente
     * pueda suplantar a otro usuario enviando un id arbitrario.
     *
     * 201 → valoración guardada
     * 400 / 401 / 404 / 409 / 500 → gestionados por GlobalExceptionHandler
     */
    @PostMapping("/valorar")
    public ResponseEntity<?> valorarPuzzle(@RequestBody ValorarPuzzleDTO dto,
                                           HttpServletRequest request)
            throws ArgumentException, ObjectNotExist, DuplicateEntry, DataBaseAccessException {

        // Extraer y validar el token JWT del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autenticación requerido");
        }

        Integer idUsuarioToken;
        try {
            String token = authHeader.substring(7); // quitar "Bearer "
            idUsuarioToken = JwtUtil.getIdUsuario(token);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido o expirado");
        }

        // Sobreescribir el idUsuario del DTO con el del token (fuente de verdad)
        dto.setIdUsuario(idUsuarioToken);

        rankingService.valorarPuzzle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Valoración registrada");
    }
}