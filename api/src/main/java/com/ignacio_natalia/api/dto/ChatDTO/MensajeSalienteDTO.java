package com.ignacio_natalia.api.dto.ChatDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// MensajeSalienteDTO.java — lo que devuelve el server
public class MensajeSalienteDTO {
    private Long idMensaje;
    private Long idConversacion;
    private Integer idUsuario;
    private String nombre;
    private String contenido;
    private OffsetDateTime creadoEn;
}