package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "valoracion_puzzle",
        schema = "puzzles",
        // Restricción única: un usuario no valora el mismo puzzle dos veces
        uniqueConstraints = @UniqueConstraint(
                name = "uq_valoracion_usuario_puzzle",
                columnNames = {"id_puzzle", "id_usuario"}
        )
)
public class ValoracionPuzzle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion", nullable = false)
    private Integer id;

    /** Puntuación 1-5 */
    @Column(name = "valoracion", nullable = false)
    private Integer valoracion;

    /** Fecha/hora con zona horaria (TIMESTAMPTZ en PostgreSQL) */
    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_puzzle", nullable = false)
    private Puzzle puzzle;

    /** Usuario que emite la valoración */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}