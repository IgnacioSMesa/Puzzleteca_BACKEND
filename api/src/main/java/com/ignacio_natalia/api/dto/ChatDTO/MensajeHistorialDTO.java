package com.ignacio_natalia.api.dto.ChatDTO;

import com.ignacio_natalia.api.modelo.Mensaje;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensajeHistorialDTO {
    private Long idMensaje;
    private Long idConversacion;
    private Integer idUsuario;
    private String nombre;
    private String contenido;
    private OffsetDateTime creadoEn;

    public static MensajeHistorialDTO fromEntity(Mensaje m) {
        MensajeHistorialDTO dto = new MensajeHistorialDTO();
        dto.setIdMensaje(m.getId());
        dto.setIdConversacion(m.getIdConversation().getId());
        dto.setIdUsuario(m.getIdUsuario().getId());
        dto.setNombre(m.getIdUsuario().getNombre());
        dto.setContenido(m.getContenido());
        dto.setCreadoEn(m.getCreadoEn());
        return dto;
    }
}