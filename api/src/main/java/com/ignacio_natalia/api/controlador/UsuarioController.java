package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.config.JwtUtil;
import com.ignacio_natalia.api.dto.LoginRequest;
import com.ignacio_natalia.api.dto.LoginResponse;
import com.ignacio_natalia.api.exepciones.ErrorCode;
import com.ignacio_natalia.api.exepciones.ObjectNotExist;
import com.ignacio_natalia.api.exepciones.OperationException;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String registrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Llamar al servicio para insertar el usuario en Supabase
            interfazDAO.insertarUsuario(usuario);
            return "Usuario registrado exitosamente";
        } catch (Exception e) {
            return "Error al registrar el usuario: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws ObjectNotExist, OperationException {

        Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(request.getEmail());
        if (usuario.isEmpty()) throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);

        Usuario user = usuario.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswd())) {
            throw new OperationException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtUtil.generarToken(user.getEmail());
        logger.info("Token generado: {}", token);

        return new LoginResponse(token);
    }
}