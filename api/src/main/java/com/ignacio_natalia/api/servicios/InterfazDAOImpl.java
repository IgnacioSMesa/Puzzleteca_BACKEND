package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.*;
import com.ignacio_natalia.api.repositorio.PuzzleRepository;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Comparator;
import java.util.List;

@Service // lógica de negocio
@Transactional // si algo falla deshace todo
public class InterfazDAOImpl implements InterfazDAO {

    @Autowired
    private final UsuarioRepository usuarioRepo;

    @Autowired
    private final PuzzleRepository puzzleRepo;
    private final Logger logger = LoggerFactory.getLogger(InterfazDAOImpl.class);

    public InterfazDAOImpl(UsuarioRepository usuarioRepo, PuzzleRepository puzzleRepo) {
        this.usuarioRepo = usuarioRepo;
        this.puzzleRepo = puzzleRepo;
    }
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void insertarUsuario(Usuario user) throws ArgumentException, DataBaseAccessException, DuplicateEntry, OperationException {

        if (user == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            // Verificamos si ya existe el usuario
            if (usuarioRepo.findUsuarioByEmail(user.getEmail()).isPresent()) {
                System.out.println("Estoy en el if de duplicados, lanzo excepcion");
                throw new DuplicateEntry(ErrorCode.DUPLICATE_ENTRY);
            }

            // Hasheamos la contraseña antes de guardar
            String hashedPassword = passwordEncoder.encode(user.getPasswd());
            user.setPasswd(hashedPassword);

            // Insertamos el usuario
            Usuario usuarioGuardado = usuarioRepo.save(user);

            if (usuarioGuardado.getEmail() == null || usuarioGuardado.getPasswd() == null) {
                throw new OperationException(ErrorCode.OPERATION_ERROR);
            }

        } catch (org.springframework.dao.DataAccessException ex) {
            logger.error("Error de acceso a base de datos al crear usuario {}", user.getEmail(), ex);
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }

    @Override
    public void insertarPuzzle(Puzzle puzzle) throws ArgumentException, DataBaseAccessException, OperationException {

        if (puzzle == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            Puzzle puzzleGuardado = puzzleRepo.save(puzzle);

            if (puzzleGuardado.getId() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

    }

    @Override
    public void eliminarCuenta(String email) throws ArgumentException, DataBaseAccessException, ObjectNotExist, OperationException {

        if (email.isEmpty()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            int filasAfectadas = usuarioRepo.deleteByEmailReturnCount(email);

            if (filasAfectadas == 0) {
                throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);
            }

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

    }

    @Override
    public void actualizarUsuario(String email, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

        if (email == null || email.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (atributo == null || atributo.isBlank() || cambio == null || cambio.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            Usuario usuarioActualizar = usuarioRepo.findUsuarioByEmail(email).orElseThrow(() -> new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND));

            boolean actualizado = false;

            switch (atributo) {

                case "nombre":

                    if (!cambio.equals(usuarioActualizar.getNombre())) {
                        usuarioActualizar.setNombre(cambio);
                        actualizado = true;
                    }
                    break;

                case "apellido":
                    if (!cambio.equals(usuarioActualizar.getApellido())) {
                        usuarioActualizar.setApellido(cambio);
                        actualizado = true;
                    }
                    break;

                default:
                    throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

            }

            if (!actualizado) throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);

            usuarioRepo.save(usuarioActualizar);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

    }

    @Override
    public void actualizarPuzzle(Integer id_usuario, Integer id_puzzle, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

    }

    @Override
    public List<Usuario> listarUsuarios() throws DataBaseAccessException, DataEmptyAccess {

        try {

            List<Usuario> usuarios = usuarioRepo.findAll();

            if (usuarios.isEmpty()) throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);

            return usuarios;

        } catch (org.springframework.dao.DataAccessException ex) {

            // Capturo una excepcion de spring en caso de que falle al entrar a la base de datos
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }

    @Override
    public List<Puzzle> listarPuzzles() throws DataBaseAccessException, DataEmptyAccess {

        try {

            List<Puzzle> puzzles = puzzleRepo.findAll();

            if (puzzles.isEmpty()) throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);

            return puzzles;

        }  catch (org.springframework.dao.DataAccessException ex) {

            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }

    @Override
    public void cambiarEstadoUsuario(String email, Usuario.TipoUsuario tipo) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

    }

    @Override
    public int mejorTiempo() throws DataBaseAccessException, DataEmptyAccess {

        try {

            Integer mejor = puzzleRepo.obtenerMejorTiempo();

            if (mejor == null) {
                throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);
            }

            return mejor;

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }
}
