package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.dto.RankingDTO.RankingDiarioDTO;
import com.ignacio_natalia.api.modelo.ValoracionPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValoracionPuzzleRepository extends JpaRepository<ValoracionPuzzle, Integer> {

    /**
     * Ranking diario: media y total de valoraciones recibidas HOY, por usuario.
     *
     * Usa SQL nativa para aprovechar el índice idx_valoracion_fecha y la
     * función DATE() de PostgreSQL directamente.
     *
     * NOTA: el ORDER de columnas del SELECT debe coincidir con el constructor
     *       de RankingDiarioDTO (idUsuario, nombre, apellido, mediaDiaria, total).
     */
    @Query(value = """
            SELECT u.id_usuario,
                   u.nombre,
                   u.apellido,
                   AVG(v.valoracion)      AS media_diaria,
                   COUNT(v.id_valoracion) AS total_valoraciones
            FROM   puzzles.usuario u
            JOIN   puzzles.puzzle  p ON p.id_usuario = u.id_usuario
            JOIN   puzzles.valoracion_puzzle v ON v.id_puzzle = p.id_puzzle
            WHERE  DATE(v.fecha) = CURRENT_DATE
            GROUP  BY u.id_usuario, u.nombre, u.apellido
            HAVING COUNT(v.id_valoracion) > 0
            ORDER  BY media_diaria DESC
            """,
            nativeQuery = true)
    List<Object[]> obtenerRankingDiarioRaw();

    /**
     * Comprueba si un usuario ya valoró un puzzle concreto hoy.
     * Evita duplicados a nivel de aplicación (la BD tiene la constraint uq_).
     */
    @Query(value = """
            SELECT COUNT(*) > 0
            FROM   puzzles.valoracion_puzzle v
            WHERE  v.id_puzzle  = :idPuzzle
              AND  v.id_usuario = :idUsuario
            """,
            nativeQuery = true)
    boolean yaValorado(@Param("idPuzzle")  Integer idPuzzle,
                       @Param("idUsuario") Integer idUsuario);

    /** Busca la valoración existente (para actualizar si el negocio lo permitiese). */
    @Query("SELECT v FROM ValoracionPuzzle v WHERE v.puzzle.id = :idPuzzle AND v.usuario.id = :idUsuario")
    Optional<ValoracionPuzzle> findByPuzzleAndUsuario(@Param("idPuzzle")  Integer idPuzzle,
                                                      @Param("idUsuario") Integer idUsuario);
}