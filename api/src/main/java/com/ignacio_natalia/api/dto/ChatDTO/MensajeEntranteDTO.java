package com.ignacio_natalia.api.dto.ChatDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// MensajeEntranteDTO.java — lo que envía el cliente
// MensajeEntranteDTO — añade email
public class MensajeEntranteDTO {
    private Long idConversacion;
    private String contenido;
    private Integer idUsuario;
}
