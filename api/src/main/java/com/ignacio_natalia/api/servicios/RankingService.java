package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.dto.PuzzlesDTO.ValorarPuzzleDTO;
import com.ignacio_natalia.api.dto.RankingDTO.RankingDiarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.modelo.ValoracionPuzzle;
import com.ignacio_natalia.api.repositorio.PuzzleRepository;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import com.ignacio_natalia.api.repositorio.ValoracionPuzzleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final ValoracionPuzzleRepository valoracionRepo;
    private final PuzzleRepository           puzzleRepo;
    private final UsuarioRepository          usuarioRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public RankingService(ValoracionPuzzleRepository valoracionRepo,
                          PuzzleRepository puzzleRepo,
                          UsuarioRepository usuarioRepo) {
        this.valoracionRepo = valoracionRepo;
        this.puzzleRepo     = puzzleRepo;
        this.usuarioRepo    = usuarioRepo;
    }

    // ------------------------------------------------------------------
    //  RANKING DIARIO
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<RankingDiarioDTO> obtenerRankingDiario() throws DataBaseAccessException {
        try {
            List<Object[]> filas = valoracionRepo.obtenerRankingDiarioRaw();
            return filas.stream().map(row -> new RankingDiarioDTO(
                    ((Number) row[0]).intValue(),
                    (String)  row[1],
                    (String)  row[2],
                    ((Number) row[3]).doubleValue(),
                    ((Number) row[4]).longValue()
            )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    // ------------------------------------------------------------------
    //  VALORAR PUZZLE
    // ------------------------------------------------------------------

    @Transactional
    public void valorarPuzzle(ValorarPuzzleDTO dto)
            throws ArgumentException, ObjectNotExist, DuplicateEntry, DataBaseAccessException {

        if (dto.getValoracion() == null || dto.getValoracion() < 1 || dto.getValoracion() > 5) {
            throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        }

        if (valoracionRepo.yaValorado(dto.getIdPuzzle(), dto.getIdUsuario())) {
            throw new DuplicateEntry(ErrorCode.DUPLICATE_ENTRY);
        }

        Puzzle puzzle = puzzleRepo.findById(dto.getIdPuzzle())
                .orElseThrow(() -> new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND));

        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND));

        ValoracionPuzzle valoracion = new ValoracionPuzzle();
        valoracion.setPuzzle(puzzle);
        valoracion.setUsuario(usuario);
        valoracion.setValoracion(dto.getValoracion());
        valoracion.setFecha(OffsetDateTime.now());

        try {
            valoracionRepo.save(valoracion);

            // Flush para que la nueva valoración sea visible en la query AVG
            entityManager.flush();

            // Recalcular media y total desde la tabla de valoraciones
            Double media = valoracionRepo.calcularMedia(dto.getIdPuzzle());
            Long total   = valoracionRepo.contarValoraciones(dto.getIdPuzzle());

            puzzle.setValoracion_media(media != null ? media : 0.0);
            puzzle.setTotal_valoraciones(total != null ? total.intValue() : 0);
            puzzleRepo.save(puzzle);

        } catch (Exception e) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }
}