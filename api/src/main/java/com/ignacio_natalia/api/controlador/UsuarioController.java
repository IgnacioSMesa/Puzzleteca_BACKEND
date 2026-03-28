package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.config.JwtUtil;
import com.ignacio_natalia.api.dto.Login.LoginRequest;
import com.ignacio_natalia.api.dto.Login.LoginResponse;
import com.ignacio_natalia.api.dto.UsuariosDTO.ActualizarUsuarioDTO;
import com.ignacio_natalia.api.dto.UsuariosDTO.UsuarioDTO;
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

            String token = JwtUtil.generarToken(user.getEmail(), user.getTipoUsuario());
            logger.info("Token generado: {}", token);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error en el login", 500));
        }
    }

    @GetMapping("/listarUsuarios")
    public ResponseEntity<?> listarUsuarios() {
        try {
            List<Usuario> usuarios = interfazDAO.listarUsuarios();
            List<UsuarioDTO> usuarioDTOS = usuarios.stream()
                    .map(UsuarioDTO::fromEntity)
                    .toList();

            return ResponseEntity.ok(usuarioDTOS);

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 409));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay usuarios registrados", 404));
        }
    }

    @PutMapping("/actualizarUsuario")
    public ResponseEntity<?> actualizarUsuario(@RequestBody ActualizarUsuarioDTO dto) {
        try {

            interfazDAO.actualizarUsuario(dto.getEmail(), dto.getAtributo(), dto.getCambio());
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (ObjectNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", 404));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El usuario ya tiene ese valor, no hay nada que actualizar", 409));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));

        } catch (OperationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar el usuario", 500));
        }
    }

    @PutMapping("/cambiarEstado")
    public ResponseEntity<?> cambiarEstado(@RequestParam String email, @RequestParam Usuario.TipoUsuario tipo) {
        try {
            interfazDAO.cambiarEstadoUsuario(email, tipo);
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (ObjectNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", 404));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El usuario ya tiene ese estado", 409));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));

        } catch (OperationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al cambiar el estado del usuario", 500));
        }
    }

    @DeleteMapping("/eliminarCuenta")
    public ResponseEntity<?> eliminarCuenta(@RequestParam String email) {
        try {
            interfazDAO.eliminarCuenta(email);
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (ObjectNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", 404));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));

        } catch (OperationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al eliminar la cuenta", 500));
        }
    }
}