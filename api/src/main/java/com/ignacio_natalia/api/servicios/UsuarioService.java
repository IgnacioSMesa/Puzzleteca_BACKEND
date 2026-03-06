package com.ignacio_natalia.api.servicios;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioService {

    public static String encriptarPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}