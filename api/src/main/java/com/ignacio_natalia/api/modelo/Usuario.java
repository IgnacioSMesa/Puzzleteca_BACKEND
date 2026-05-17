package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuario", schema = "puzzles")
public class Usuario {

    public enum TipoUsuario {
        Invitado,
        Usuario,
        Admin,
        Bloqueado
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Size(max = 150)
    @NotNull
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "contrasenna", nullable = false)
    private String contrasenna;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipousuario", length = 50)
    private TipoUsuario tipoUsuario = TipoUsuario.Usuario;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Mensaje> mensajes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuario")
    private Set<ParticipantesConversacion> participantesConversacions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuario", fetch = FetchType.EAGER)
    private Set<Puzzle> puzzles = new LinkedHashSet<>();

}