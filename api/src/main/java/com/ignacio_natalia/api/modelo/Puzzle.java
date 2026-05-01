package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "puzzle", schema = "puzzles")
public class Puzzle {

    public enum Dificultades { Facil, Media, Dificil, Extremo }
    public enum Estados { Publico, Privado, Bloqueado }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puzzle", nullable = false)
    private Integer id;

    @Size(max = 150)
    @Column(name = "titulo", length = 150)
    private String titulo;

    @Size(max = 150)
    @Column(name = "autor", length = 150)
    private String autor;

    @Column(name = "tiempo")
    private Integer tiempo;

    @Column(name = "piezas")
    private Integer piezas;

    @Enumerated(EnumType.STRING)
    @Column(name = "dificultad", length = 50)
    private Dificultades dificultad;

    @Column(name = "descripcion", length = Integer.MAX_VALUE)
    private String descripcion;

    @Column(name = "color")
    private boolean color;

    @Column(name = "valoracion_media")
    private int valoracion_media;

    // Ahora guarda el base64 directamente en BD, no una ruta
    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private Estados estado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;
}