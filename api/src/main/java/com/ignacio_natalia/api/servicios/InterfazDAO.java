package com.ignacio_natalia.api.servicios;

import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.*;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ignacio y Natalia
 */
public interface InterfazDAO {

    /**
     * Inserta un nuevo usuario en el sistema.
     *
     * @param user objeto de tipo Usuario que se desea insertar
     * @throws ArgumentException si al menos un argumento es invalido
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DuplicateEntry si el usuario ya existe
     * @throws OperationException si ocurre un error durante la inserción
     */
    void insertarUsuario(Usuario user) throws ArgumentException, DataBaseAccessException, DuplicateEntry, OperationException;

    // Este metodo no lanza la excepcion de DuplicateEntry porque varios usuarios pueden tener puzzles repetidos
    /**
     * Inserta un nuevo puzzle en el sistema.
     *
     * @param puzzle objeto Puzzle que se desea insertar
     * @throws ArgumentException si al menos un argumento es invalido
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws OperationException si ocurre un error durante la inserción
     */
    void insertarPuzzle(Puzzle puzzle) throws ArgumentException, DataBaseAccessException, OperationException;

    /**
     * Elimina una cuenta de usuario identificada por su email.
     *
     * @param email email del usuario que se desea eliminar
     * @throws ArgumentException si el email no es válido
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws ObjectNotExist si el usuario no existe
     * @throws OperationException si ocurre un error durante la eliminación
     */
    void eliminarCuenta(String email) throws ArgumentException, DataBaseAccessException, ObjectNotExist, OperationException;

    /**
     * Actualiza un atributo concreto de un usuario.
     *
     * @param email email del usuario a actualizar
     * @param atributo nombre del atributo que se desea modificar
     * @param cambio nuevo valor del atributo
     * @throws ArgumentException si los argumentos no son válidos
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen datos
     * @throws ObjectNotExist si el usuario no existe
     * @throws OperationException si ocurre un error durante la actualización
     */
    void actualizarUsuario(String email, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException;

    /**
     * Actualiza un atributo concreto de un puzzle perteneciente a un usuario.
     *
     * @param id_usuario identificador del usuario propietario del puzzle
     * @param id_puzzle identificador del puzzle
     * @param atributo nombre del atributo a modificar
     * @param cambio nuevo valor del atributo
     * @throws ArgumentException si los argumentos no son válidos
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen datos
     * @throws ObjectNotExist si el puzzle o el usuario no existen
     * @throws OperationException si ocurre un error durante la actualización
     */
    void actualizarPuzzle(Integer id_usuario, Integer id_puzzle, String atributo, String cambio) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException;

    /**
     * Obtiene un listado con todos los usuarios registrados.
     *
     * @return lista de objetos Usuario
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen usuarios
     */
    List<Usuario> listarUsuarios() throws DataBaseAccessException, DataEmptyAccess;

    /**
     * Obtiene un listado con todos los puzzles almacenados.
     *
     * @return lista de objetos Puzzle
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen puzzles
     */
    List<Puzzle> listarPuzzles() throws DataBaseAccessException, DataEmptyAccess;

    /**
     * Bloquea o desbloquea un usuario impidiendo su uso dentro del sistema o devolviendolo.
     *
     * @param email email del usuario a bloquear o desbloquear
     * @throws ArgumentException si el email no es válido
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen datos
     * @throws ObjectNotExist si el usuario no existe
     * @throws OperationException si ocurre un error durante el bloqueo o desbloqueo
     */
    void cambiarEstadoUsuario(String email, Usuario.TipoUsuario tipo) throws ArgumentException, DataBaseAccessException, DataEmptyAccess, ObjectNotExist, OperationException;

    /**
     * Cambia el estado del puzzle entre Publico, Privado o Bloqueado
     *
     * @throws ArgumentException si el id del  usuario no es válido o no está
     * @throws DataBaseAccessException si ocurre un error de acceso a datos
     * @throws DataEmptyAccess si no existen datos
     */
    void cambiarEstadoPuzzle(Integer id_usuario, Integer id_puzzle , Puzzle.Estados tipo) throws ArgumentException, DataBaseAccessException, DataEmptyAccess;

    // =========================================================================
    // POST (foro)
    // =========================================================================

    /**
     * Crea un nuevo post. Si se incluye imagen, la procesa (redimensiona +
     * comprime) y guarda en disco; la ruta relativa se almacena en BD.
     *
     * @param idUsuario ID del usuario autor
     * @param contenido texto del post (puede ser null si hay imagen)
     * @param imagen    archivo de imagen (puede ser null si hay contenido)
     * @return el Post persistido
     * @throws ArgumentException       si faltan datos obligatorios
     * @throws DataBaseAccessException si falla el acceso a BD
     * @throws OperationException      si el guardado no produce resultado
     * @throws IOException             si falla el procesamiento de la imagen
     */
    Post crearPost(Integer idUsuario, String contenido, org.springframework.web.multipart.MultipartFile imagen) throws ArgumentException, DataBaseAccessException, OperationException, java.io.IOException;

    /**
     * Devuelve el feed paginado de posts (más recientes primero).
     *
     * @param pagina  número de página (0-indexed)
     * @param tamanno cantidad de posts por página
     * @return página de Posts
     * @throws DataBaseAccessException si falla el acceso a BD
     * @throws DataEmptyAccess         si no hay posts
     */
    org.springframework.data.domain.Page<Post> listarPosts(int pagina, int tamanno) throws DataBaseAccessException, DataEmptyAccess;

    /**
     * Elimina un post y su imagen asociada en disco.
     *
     * @param idPost    ID del post a eliminar
     * @param idUsuario ID del usuario que solicita la eliminación
     * @throws ArgumentException       si los IDs son inválidos
     * @throws ObjectNotExist          si el post no existe o no pertenece al usuario
     * @throws DataBaseAccessException si falla el acceso a BD
     */
    void eliminarPost(Integer idPost, Integer idUsuario) throws ArgumentException, ObjectNotExist, DataBaseAccessException;

}