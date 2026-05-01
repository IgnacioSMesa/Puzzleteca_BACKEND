package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "post", schema = "puzzles")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post", nullable = false)
    private Integer id;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    /**
     * Ruta relativa del archivo de imagen almacenado en servidor.
     * Ej: "posts/2024/uuid.webp"
     * Se mantiene TEXT en BD para flexibilidad con distintos
     * proveedores de almacenamiento (local, S3, CDN…).
     */
    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imagenUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @ColumnDefault("0")
    @Column(name = "total_likes", nullable = false)
    private Integer totalLikes = 0;

    @ColumnDefault("0")
    @Column(name = "total_comentarios", nullable = false)
    private Integer totalComentarios = 0;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @OneToMany(mappedBy = "idPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ComentarioPost> comentarios = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) this.fechaCreacion = OffsetDateTime.now();
    }
}