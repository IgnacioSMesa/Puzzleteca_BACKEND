package com.ignacio_natalia.api.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "conversacion", schema = "puzzles")
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creado_en")
    private OffsetDateTime creadoEn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "actualizado_en")
    private OffsetDateTime actualizadoEn;

    @OneToMany(mappedBy = "idConversation")
    private Set<Mensaje> mensajes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idConversation")
    private Set<ParticipantesConversacion> participantesConversacions = new LinkedHashSet<>();


}