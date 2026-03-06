package com.ignacio_natalia.api.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ignacio_natalia.api.modelo.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.apiKey}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate;

    public SupabaseService() {
        this.restTemplate = new RestTemplate();
    }

    public void insertarUsuario(Usuario usuario) throws Exception {
        // URL de Supabase (asume que tienes una tabla llamada "usuario")
        String url = supabaseUrl + "/rest/v1/usuario"; // Asegúrate que la tabla se llama "usuario"

        // Crear los encabezados para la autenticación
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseApiKey);
        headers.set("Authorization", "Bearer " + supabaseApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear el cuerpo de la solicitud con los datos del usuario
        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellido", usuario.getApellido());
        usuarioData.put("email", usuario.getEmail());
        usuarioData.put("passwd", UsuarioService.encriptarPassword(usuario.getPasswd()));
        usuarioData.put("tipousuario", usuario.getTipousuario());

        // Convertir el cuerpo del usuario a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(usuarioData);

        // Configurar la solicitud HTTP
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Enviar la solicitud HTTP a Supabase
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // Verificar que la solicitud se haya completado correctamente
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new Exception("Error al insertar el usuario en Supabase: " + response.getStatusCode());
        }
    }
}