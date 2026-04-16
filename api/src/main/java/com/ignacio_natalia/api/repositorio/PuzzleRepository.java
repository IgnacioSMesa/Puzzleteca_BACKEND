package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Integer> {


    @Query("SELECT MIN(p.tiempo) FROM Puzzle p")
    Integer obtenerMejorTiempo();

    @Query("SELECT p FROM Puzzle p WHERE p.id = :id_puzzle AND p.idUsuario.id = :id_usuario")
    Optional<Puzzle> findByIdAndUsuarioId(@Param("id_puzzle") Integer id_puzzle, @Param("id_usuario") Integer id_usuario);

}
