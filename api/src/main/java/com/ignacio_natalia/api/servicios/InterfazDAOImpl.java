package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.*;
import com.ignacio_natalia.api.repositorio.RepositorioPuzzle;
import com.ignacio_natalia.api.repositorio.RepositorioUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void insertarUsuario(Usuario user) throws ArgumentException, DataAccessException, DuplicateEntry, OperationException {

    }

    @Override
    public void insertarPuzzle(Puzzle puzzle) throws ArgumentException, DataAccessException, OperationException {

    }

    @Override
    public void eliminarCuenta(String email) throws ArgumentException, DataAccessException, ObjectNotExist, OperationException {

    }

    @Override
    public void actualizarUsuario(String email, String atributo, String cambio) throws ArgumentException, DataAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

    }

    @Override
    public void actualizarPuzzle(Integer id_usuario, Integer id_puzzle, String atributo, String cambio) throws ArgumentException, DataAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

    }

    @Override
    public List<Usuario> listarUsuarios() throws ArgumentException, DataAccessException, DataEmptyAccess {

        return usuarioRepo.findAll();
    }

    @Override
    public List<Puzzle> listarPuzzles() throws ArgumentException, DataAccessException, DataEmptyAccess {
        return List.of();
    }

    @Override
    public void cambiarEstadoUsuario(String email, Usuario.TipoUsuario tipo) throws ArgumentException, DataAccessException, DataEmptyAccess, ObjectNotExist, OperationException {

    }

    @Override
    public Puzzle[] topCinco() throws ArgumentException, DataAccessException, DataEmptyAccess {
        return new Puzzle[0];
    }

    @Override
    public int mejorTiempo() throws DataAccessException, DataEmptyAccess {
        return 0;
    }
}
