package com.ignacio_natalia.api.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Integer id_usuario;
    private String tipoUsuario;
    private String nombre;

}