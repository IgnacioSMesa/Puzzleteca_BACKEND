package com.ignacio_natalia.api.dto;

import com.ignacio_natalia.api.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;
}
