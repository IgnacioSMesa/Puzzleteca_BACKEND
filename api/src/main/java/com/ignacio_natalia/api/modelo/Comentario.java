package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "comentario", schema = "puzzles")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer idComentario;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_puzzle", nullable = false)
    private Integer idPuzzle;

    // --- Hooks ---
    @PrePersist
    public void prePersist() {
        this.creadoEn = OffsetDateTime.now();
    }

    // --- Getters & Setters ---
    public Integer getIdComentario() { return idComentario; }
    public void setIdComentario(Integer idComentario) { this.idComentario = idComentario; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdPuzzle() { return idPuzzle; }
    public void setIdPuzzle(Integer idPuzzle) { this.idPuzzle = idPuzzle; }
}