package com.ignacio_natalia.api.repositorio;

import com.ignacio_natalia.api.modelo.ParticipantesConversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantesRepository extends JpaRepository<ParticipantesConversacion, Long> {

    boolean existsByIdConversationIdAndIdUsuarioId(Long idConversacion, Integer idUsuario);

    List<ParticipantesConversacion> findByIdConversationId(Long idConversacion);

    List<ParticipantesConversacion> findByIdUsuarioId(Integer idUsuario);

    @Query("SELECT p.idConversation.id FROM ParticipantesConversacion p WHERE p.idUsuario.id IN :ids GROUP BY p.idConversation.id HAVING COUNT(DISTINCT p.idUsuario.id) = :total")
    List<Long> findConversacionByParticipantes(@Param("ids") List<Integer> ids, @Param("total") long total);


}
