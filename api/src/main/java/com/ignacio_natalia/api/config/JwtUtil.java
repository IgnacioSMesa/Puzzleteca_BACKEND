package com.ignacio_natalia.api.config;

import com.ignacio_natalia.api.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generarToken(String email, Usuario.TipoUsuario tipoUsuario) {

        return Jwts.builder()
                .setSubject(email)
                .claim("tipoUsuario", tipoUsuario.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

}