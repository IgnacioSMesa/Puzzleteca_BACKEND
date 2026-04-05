package com.ignacio_natalia.api.servicios;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CodigoVerificacionStore {

    private static final int MINUTOS_EXPIRACION = 10;
    private record EntradaCodigo(String codigo, LocalDateTime expiracion) {}
    private final Map<String, EntradaCodigo> codigos = new ConcurrentHashMap<>();

    public void guardar(String email, String codigo) {
        codigos.put(email, new EntradaCodigo(codigo, LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION)));
    }

    public boolean validar(String email, String codigo) {

        EntradaCodigo entrada = codigos.get(email);

        if (entrada == null) return false;
        if (LocalDateTime.now().isAfter(entrada.expiracion)) {
            codigos.remove(email);
            return false;
        }

        return entrada.codigo().equals(codigo);
    }

    public void eliminar(String email) {
        codigos.remove(email);
    }
}
