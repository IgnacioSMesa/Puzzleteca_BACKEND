package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.*;
import com.ignacio_natalia.api.repositorio.RepositorioPuzzle;
import com.ignacio_natalia.api.repositorio.RepositorioUsuario;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service // lógica de negocio
@Transactional // si algo falla deshace todo
public class InterfazDAOImpl implements InterfazDAO {

    @Autowired
    private final RepositorioUsuario usuarioRepo;

    @Autowired
    private final RepositorioPuzzle puzzleRepo;

    public InterfazDAOImpl(RepositorioUsuario usuarioRepo, RepositorioPuzzle puzzleRepo) {
        this.usuarioRepo = usuarioRepo;
        this.puzzleRepo = puzzleRepo;
    }

    @Override
    public void insertarUsuario(Usuario user) throws ArgumentException, DataBaseAccessException, DuplicateEntry, OperationException {

        // Comprobamos que el usuario no sea null
        if (user == null) throw new ArgumentException(ErrorCode.INVALID_ARGUMENT);

        try {

            // Comprobamos si existe en la base de datos
            if (usuarioRepo.findUsuarioByEmail(user.getEmail()).isPresent()) {
                throw new DuplicateEntry(ErrorCode.DUPLICATE_ENTRY);
            }

            // Lo insertamos
            Usuario usuarioGuardado = usuarioRepo.save(user);

            // Comprobamos si el usuario se ha insertado, en caso de que no, lanza excepcion
            if (usuarioGuardado.getEmail() == null) throw new OperationException(ErrorCode.OPERATION_ERROR);

        } catch (org.springframework.dao.DataAccessException ex) {
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
    public Puzzle[] topCinco() throws ArgumentException, DataBaseAccessException, DataEmptyAccess {
        List<Puzzle> puzzles = listarPuzzles();
        puzzles.sort(Comparator.comparingDouble(Puzzle::getValoracion).reversed());
        return puzzles.stream().limit(5).toArray(Puzzle[]::new);
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
