package com.ignacio_natalia.api.dto.EmailDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmarCambioPasswordDTO {
    private String email;
    private String codigo;
    private String nuevaContrasena;
}