package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioPuzzle extends JpaRepository<Puzzle, Integer> {

    // modificar a futuro
    List<Puzzle> findTop5ByOrderByValoracionDesc();

    @Query("SELECT MIN(p.tiempo) FROM Puzzle p")
    Integer obtenerMejorTiempo();
}
