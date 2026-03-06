package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private InterfazDAO interfazDAO;

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Llamar al servicio para insertar el usuario en Supabase
            interfazDAO.insertarUsuario(usuario);
            return "Usuario registrado exitosamente";
        } catch (Exception e) {
            return "Error al registrar el usuario: " + e.getMessage();
        }
    }
}