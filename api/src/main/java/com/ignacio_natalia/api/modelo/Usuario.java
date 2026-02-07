package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuarios", schema = "puzzlesbbdd")
public class Usuario {

    public enum TipoUsuario {Admin, Bloqueado, Usuario}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellido", length = 50)
    private String apellido;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "passwd", length = 100)
    private String passwd;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipousuario;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Puzzle> puzzles = new LinkedHashSet<>();

}