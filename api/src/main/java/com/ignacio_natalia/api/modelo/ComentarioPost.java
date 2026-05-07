package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

/**
 * Comentarios de un Post del foro.
 * Mapea la tabla puzzles.comentario, que referencia a id_post.
 */
@Getter
@Setter
@Entity
@Table(name = "comentario", schema = "puzzles")
public class ComentarioPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_post", nullable = false)
    private Post idPost;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) this.fechaCreacion = OffsetDateTime.now();
    }
}