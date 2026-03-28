package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.ParticipantesConversacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantesRepository extends JpaRepository<ParticipantesConversacion, Long> {

    boolean existsByIdConversationIdAndIdUsuarioId(Long idConversacion, Integer idUsuario);

    List<ParticipantesConversacion> findByIdConversationId(Long idConversacion);

    List<ParticipantesConversacion> findByIdUsuarioId(Integer idUsuario);
}
