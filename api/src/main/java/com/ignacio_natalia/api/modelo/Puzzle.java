package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "puzzles", schema = "puzzlesbbdd")
public class Puzzle {

    public enum Dificultades {Facil, Medio, Dificil, Extremo}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puzzle", nullable = false)
    private Integer id;

    @Column(name = "autor", length = 50)
    private String autor;

    @Column(name = "tiempo")
    private Integer tiempo;

    @Column(name = "piezas")
    private Integer piezas;

    @Enumerated(EnumType.STRING)
    private Dificultades dificultad;

    @Column(name = "descripcion", length = 5000)
    private String descripcion;

    @Column(name = "color")
    private Boolean color;

    @Column(name = "valoracion")
    private Integer valoracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;


}