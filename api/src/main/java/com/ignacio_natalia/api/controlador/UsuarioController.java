package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.config.JwtUtil;
import com.ignacio_natalia.api.dto.LoginRequest;
import com.ignacio_natalia.api.dto.LoginResponse;
import com.ignacio_natalia.api.dto.UsuarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.ErrorResponse;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private InterfazDAO interfazDAO;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {

        try {
            interfazDAO.insertarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");

        } catch (DuplicateEntry e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Ya existe un usuario con ese email", 409));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al registrar el usuario", 500));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(request.getEmail());

            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Usuario no encontrado", 404));
            }

            Usuario user = usuario.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPasswd())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Contraseña incorrecta", 401));
            }

            String token = JwtUtil.generarToken(user.getEmail());
            logger.info("Token generado: {}", token);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error en el login", 500));
        }
    }
    // Actualizar campos
    // Bloquear o desbloquear usuario
    // Contraseña olvidada (se puede usar mismo end point que bloquear y desbloquear [hablar])
    // Eliminar cuenta

    // Listar usuarios
    @GetMapping("/listarUsuarios")
    public ResponseEntity<?> listarUsuarios() {
        try {

            List<Usuario> usuarios = interfazDAO.listarUsuarios();
            List<UsuarioDTO> usuarioDTOS = usuarios.stream()
                    .map(UsuarioDTO::fromEntity)
                    .toList();

            return ResponseEntity.ok(usuarioDTOS);
        } catch (DataBaseAccessException db) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 409));

        } catch (DataEmptyAccess e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay usuarios registrados", 404));
        }
    }

}