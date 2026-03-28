package com.ignacio_natalia.api.dto.UsuariosDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {
    private String email;
    private String atributo;
    private String cambio;
}