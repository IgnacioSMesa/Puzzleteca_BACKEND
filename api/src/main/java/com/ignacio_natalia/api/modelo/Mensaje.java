package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "mensaje", schema = "puzzles")
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "contenido", nullable = false, length = Integer.MAX_VALUE)
    private String contenido;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creado_en")
    private OffsetDateTime creadoEn;

    @Column(name = "leido_en")
    private OffsetDateTime leidoEn;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversacion idConversation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;


}