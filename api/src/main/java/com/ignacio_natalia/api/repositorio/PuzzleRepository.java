package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Integer> {

    @Query("SELECT MIN(p.tiempo) FROM Puzzle p")
    Integer obtenerMejorTiempo();
}
