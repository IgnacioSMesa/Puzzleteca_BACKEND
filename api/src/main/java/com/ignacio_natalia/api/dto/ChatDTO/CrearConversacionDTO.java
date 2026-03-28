package com.ignacio_natalia.api.dto.ChatDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearConversacionDTO {
    private List<String> participantes; // lista de emails

}
