package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.*;
import com.ignacio_natalia.api.repositorio.PuzzleRepository;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service // lógica de negocio
@Transactional // si algo falla deshace todo
public class InterfazDAOImpl implements InterfazDAO {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private final UsuarioRepository usuarioRepo;

    @Autowired
    private final PuzzleRepository puzzleRepo;
    //private final Logger logger = LoggerFactory.getLogger(InterfazDAOImpl.class);

    public InterfazDAOImpl(UsuarioRepository usuarioRepo, PuzzleRepository puzzleRepo) {
        this.usuarioRepo = usuarioRepo;
        this.puzzleRepo = puzzleRepo;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            String hashedPassword = passwordEncoder.encode(user.getContrasenna());
            user.setContrasenna(hashedPassword);

            // Insertamos el usuario
            Usuario usuarioGuardado = usuarioRepo.save(user);

            if (usuarioGuardado.getEmail() == null || usuarioGuardado.getContrasenna() == null) {
                throw new OperationException(ErrorCode.OPERATION_ERROR);
            }

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }

    @Override
    public void insertarPuzzle(Puzzle puzzle)
            throws ArgumentException, DataBaseAccessException, OperationException {

        if (puzzle == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            Usuario usuario = usuarioRepo.findById(puzzle.getIdUsuario().getId())
                    .orElseThrow(() -> new ArgumentException(ErrorCode.INVALID_ARGUMENT));

            puzzle.setIdUsuario(usuario);

            Puzzle guardado = puzzleRepo.save(puzzle);

            if (guardado.getId() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

        } catch (IllegalArgumentException e) {
            throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }
    }

    @Override
    public void eliminarCuenta(String email) throws ArgumentException, DataBaseAccessException, ObjectNotExist, OperationException {

        if (email == null || email.isEmpty()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        Optional<Usuario> usuario;
        int filasAfectadas;

        try {

            usuario = usuarioRepo.findUsuarioByEmail(email);
            if (usuario.isEmpty()) throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);

            filasAfectadas = usuarioRepo.deleteByEmailReturnCount(email);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

        if (filasAfectadas == 0) throw new OperationException(ErrorCode.OPERATION_ERROR);

    }

    @Override
    public void actualizarUsuario(String email, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

        if (email == null || email.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (atributo == null || atributo.isBlank() || cambio == null || cambio.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        Optional<Usuario> usuarioOpt;

        try {
            usuarioOpt = usuarioRepo.findUsuarioByEmail(email);
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

        if (usuarioOpt.isEmpty()) throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);

        Usuario usuarioActualizar = usuarioOpt.get();
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

        Usuario usuarioGuardado;

        try {

            usuarioGuardado = usuarioRepo.save(usuarioActualizar);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

        if (usuarioGuardado.getId() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

    }

    @Override
    public void actualizarPuzzle(Integer id_usuario, Integer id_puzzle, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

        if (id_usuario == null || id_puzzle == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (atributo == null || atributo.isBlank() || cambio == null || cambio.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        Optional<Puzzle> puzzleOpt;

        try {

            puzzleOpt = puzzleRepo.findByIdAndUsuarioId(id_puzzle, id_usuario);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);

        }

        if (puzzleOpt.isEmpty()) throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);

        Puzzle puzzle = puzzleOpt.get();
        boolean actualizado = false;

        switch (atributo) {
            case "autor":
                if (!cambio.equals(puzzle.getAutor())) {
                    puzzle.setAutor(cambio);
                    actualizado = true;
                }
                break;

            case "tiempo":
                Integer nuevoTiempo = Integer.parseInt(cambio);
                if (!nuevoTiempo.equals(puzzle.getTiempo())) {
                    puzzle.setTiempo(nuevoTiempo);
                    actualizado = true;
                }
                break;

            case "piezas":
                Integer nuevasPiezas = Integer.parseInt(cambio);
                if (!nuevasPiezas.equals(puzzle.getPiezas())) {
                    puzzle.setPiezas(nuevasPiezas);
                    actualizado = true;
                }
                break;

            case "dificultad":
                Puzzle.Dificultades nuevaDificultad = Puzzle.Dificultades.valueOf(cambio);
                if (!nuevaDificultad.equals(puzzle.getDificultad())) {
                    puzzle.setDificultad(nuevaDificultad);
                    actualizado = true;
                }
                break;

            case "descripcion":
                if (!cambio.equals(puzzle.getDescripcion())) {
                    puzzle.setDescripcion(cambio);
                    actualizado = true;
                }
                break;

            case "color":
                boolean nuevoColor = Boolean.parseBoolean(cambio);
                if (nuevoColor != puzzle.isColor()) {
                    puzzle.setColor(nuevoColor);
                    actualizado = true;
                }
                break;

            case "valoracion":
                int nuevaValoracion = Integer.parseInt(cambio);
                if (nuevaValoracion != puzzle.getValoracion()) {
                    puzzle.setValoracion(nuevaValoracion);
                    actualizado = true;
                }
                break;
            default:
                throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        }

        if (!actualizado) throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);

        Puzzle puzzleGuardado;

        try {

            puzzleGuardado = puzzleRepo.save(puzzle);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);

        }

        if (puzzleGuardado.getId() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

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

        if (email == null || email.isBlank()) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (tipo == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        Optional<Usuario> usuarioOpt;

        try {
            usuarioOpt = usuarioRepo.findUsuarioByEmail(email);

        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

        if (usuarioOpt.isEmpty()) throw new ObjectNotExist(ErrorCode.OBJECT_NOT_FOUND);

        Usuario usuario = usuarioOpt.get();

        if (usuario.getTipoUsuario().equals(tipo)) throw new DataEmptyAccess(ErrorCode.DATA_EMPTY);

        usuario.setTipoUsuario(tipo);

        Usuario usuarioGuardado;

        try {
            usuarioGuardado = usuarioRepo.save(usuario);
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR, ex);
        }

        if (usuarioGuardado.getId() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

    }

    @Override
    public void cambiarEstadoPuzzle(Integer id_usuario, Integer id_puzzle ,Puzzle.Estados tipo) throws ArgumentException, DataBaseAccessException, DataEmptyAccess {
        if (id_usuario == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (id_puzzle == null)throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);
        if (tipo == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            Puzzle puzzle = puzzleRepo
                    .findByIdAndUsuarioId(id_puzzle, id_usuario)
                    .orElseThrow(() -> new DataEmptyAccess(ErrorCode.OBJECT_NOT_FOUND));

            puzzle.setEstado(tipo);
            puzzleRepo.save(puzzle);

        } catch (org.springframework.dao.DataAccessException e) {
            throw new DataBaseAccessException(ErrorCode.DATA_ACCESS_ERROR);

        }

    }

}
