package com.ignacio_natalia.api.config;

import com.ignacio_natalia.api.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // Clave estática: se genera una sola vez por arranque de JVM y es
    // compartida por generarToken() y getIdUsuario()
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generarToken(String email, Integer id_usuario, Usuario.TipoUsuario tipoUsuario) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id_usuario", id_usuario)
                .claim("tipoUsuario", tipoUsuario.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    /**
     * Extrae el id_usuario del claim del token JWT.
     * Lanza JwtException si el token es inválido o ha expirado.
     */
    public static Integer getIdUsuario(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id_usuario", Integer.class);
    }
}