package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.config.JwtUtil;
import com.ignacio_natalia.api.dto.EmailDTO.ConfirmarCambioPasswordDTO;
import com.ignacio_natalia.api.dto.EmailDTO.SolicitarCodigoDTO;
import com.ignacio_natalia.api.dto.Login.LoginRequest;
import com.ignacio_natalia.api.dto.Login.LoginResponse;
import com.ignacio_natalia.api.dto.UsuariosDTO.ActualizarUsuarioDTO;
import com.ignacio_natalia.api.dto.UsuariosDTO.UsuarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.ErrorResponse;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import com.ignacio_natalia.api.servicios.CodigoVerificacionStore;
import com.ignacio_natalia.api.servicios.EmailService;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodigoVerificacionStore codigoStore;

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
            e.printStackTrace();
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

            System.out.println(user.getContrasenna());

            if (!passwordEncoder.matches(request.getContrasena(), user.getContrasenna())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Contraseña incorrecta", 401));
            }

            String token = JwtUtil.generarToken(user.getEmail(), user.getId() ,user.getTipoUsuario());
            logger.info("Token generado: {}", token);

            return ResponseEntity.ok(new LoginResponse(token, user.getId() ,user.getTipoUsuario().name()));

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

    @PostMapping("/recuperarPassword/solicitarCodigo")
    public ResponseEntity<?> solicitarCodigo(@RequestBody SolicitarCodigoDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("El email no puede estar vacío", 400));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findUsuarioByEmail(dto.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        String codigo = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        codigoStore.guardar(dto.getEmail(), codigo);

        ResponseEntity<?> response = ResponseEntity.ok().build();

        try {
            emailService.enviarCodigoVerificacion(dto.getEmail(), codigo);
        } catch (Exception ex) {
            logger.error("Error al enviar correo: {}", ex.getMessage());
        }

        return response;
    }

    @PostMapping("/recuperarPassword/confirmar")
    public ResponseEntity<?> confirmarCambioPassword(@RequestBody ConfirmarCambioPasswordDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getCodigo() == null || dto.getCodigo().isBlank()
                || dto.getNuevaContrasena() == null || dto.getNuevaContrasena().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Todos los campos son obligatorios", 400));
        }

        if (dto.getNuevaContrasena().length() < 6) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("La contraseña debe tener al menos 6 caracteres", 400));
        }

        if (!codigoStore.validar(dto.getEmail(), dto.getCodigo())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Código incorrecto o expirado", 401));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findUsuarioByEmail(dto.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", 404));
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setContrasenna(passwordEncoder.encode(dto.getNuevaContrasena()));

        try {
            usuarioRepository.save(usuario);
        } catch (Exception ex) {
            logger.error("Error al guardar la nueva contraseña para {}: {}", dto.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar la contraseña", 500));
        }

        codigoStore.eliminar(dto.getEmail());
        logger.info("Contraseña actualizada correctamente para: {}", dto.getEmail());
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}