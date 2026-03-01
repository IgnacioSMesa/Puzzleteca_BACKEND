package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.UsuarioDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.servicios.InterfazDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ControladorUsuario {

    private static final Logger logger =
            LoggerFactory.getLogger(ControladorUsuario.class);

    private final InterfazDAO dao;

    public ControladorUsuario(InterfazDAO dao) {
        this.dao = dao;
    }

    // POST /api/usuarios
    @PostMapping("/usuarios")
    public ResponseEntity<Void> crearUsuario(@RequestBody Usuario usuario)
            throws DuplicateEntry, ArgumentException, DataBaseAccessException, OperationException {

        logger.info("Creando usuario con email {}", usuario.getEmail());
        dao.insertarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/listarUsuarios
    @GetMapping("/listarUsuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios()
            throws DataEmptyAccess, ArgumentException, DataBaseAccessException {

        logger.info("Listando usuarios");
        return ResponseEntity.ok(
                dao.listarUsuarios()
                        .stream()
                        .map(usuario -> new UsuarioDTO(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getApellido(),
                                usuario.getEmail(),
                                usuario.getTipousuario()))
                        .toList()
        );
    }

    // DELETE /api/usuarios/{email}
    @DeleteMapping("/usuarios/{email}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String email)
            throws ObjectNotExist, ArgumentException, DataBaseAccessException, OperationException {

        logger.info("Eliminando usuario con email {}", email);
        dao.eliminarCuenta(email);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/usuarios/{email}
    @PatchMapping("/usuarios/{email}")
    public ResponseEntity<Void> actualizarUsuario(
            @PathVariable String email,
            @RequestBody Map<String, String> body)
            throws ObjectNotExist, DataEmptyAccess, ArgumentException, DataBaseAccessException, OperationException {

        logger.info("Actualizando usuario {} - atributo {}", email, body.get("atributo"));

        dao.actualizarUsuario(
                email,
                body.get("atributo"),
                body.get("valor")
        );
        return ResponseEntity.ok().build();
    }

    // PATCH /api/usuarios/{email}/estado?tipo=BLOQUEADO
    @PatchMapping("/usuarios/{email}/estado")
    public ResponseEntity<Void> cambiarEstadoUsuario(
            @PathVariable String email,
            @RequestParam Usuario.TipoUsuario tipo)
            throws ObjectNotExist, DataEmptyAccess, ArgumentException, DataBaseAccessException, OperationException {

        logger.info("Cambiando estado del usuario {} a {}", email, tipo);
        dao.cambiarEstadoUsuario(email, tipo);
        return ResponseEntity.ok().build();
    }
}