package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.PuzzlesDTO.ActualizarPuzzleDTO;
import com.ignacio_natalia.api.dto.PuzzlesDTO.PuzzleDTO;
import com.ignacio_natalia.api.exepciones.*;
import com.ignacio_natalia.api.modelo.Puzzle;
import com.ignacio_natalia.api.repositorio.ErrorResponse;
import com.ignacio_natalia.api.repositorio.PuzzleRepository;
import com.ignacio_natalia.api.servicios.ImagenService;
import com.ignacio_natalia.api.servicios.InterfazDAO;
import com.ignacio_natalia.api.dto.UsuariosDTO.UsuarioDTO;
import com.ignacio_natalia.api.modelo.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/puzzles")
public class PuzzleController {

    private final InterfazDAO dao;
    private final ImagenService imagenService;
    private final PuzzleRepository puzzleRepository;

    @Value("${app.image.base-url:http://localhost:8080/imagenes/}")
    private String imageBaseUrl;

    public PuzzleController(InterfazDAO dao, ImagenService imagenService, PuzzleRepository puzzleRepository) {
        this.dao = dao;
        this.imagenService = imagenService;
        this.puzzleRepository = puzzleRepository;
    }

    /**
     * POST /puzzles/registrarPuzzle
     *
     * Recibe multipart/form-data con los campos del puzzle más un fichero de imagen opcional.
     * La imagen se guarda en disco; en BD solo se almacena la ruta relativa (escalable).
     */
    @PostMapping(value = "/registrarPuzzle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPuzzle(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam Integer tiempo,
            @RequestParam Integer piezas,
            @RequestParam Puzzle.Dificultades dificultad,
            @RequestParam(required = false) String descripcion,
            @RequestParam boolean color,
            @RequestParam Puzzle.Estados estado,
            @RequestParam Integer idUsuario,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            PuzzleDTO dto = new PuzzleDTO();
            dto.setTitulo(titulo);
            dto.setAutor(autor);
            dto.setTiempo(tiempo);
            dto.setPiezas(piezas);
            dto.setDificultad(dificultad);
            dto.setDescripcion(descripcion);
            dto.setColor(color);
            dto.setEstado(estado);
            Usuario u = new Usuario();
            u.setId(idUsuario);
            dto.setUsuario(UsuarioDTO.fromEntity(u));
            dto.setValoracion(0);

            // Procesar imagen si se incluye
            if (imagen != null && !imagen.isEmpty()) {
                String rutaRelativa = imagenService.guardarImagenPuzzle(imagen);
                dto.setImagenUrl(rutaRelativa);
            }

            dao.insertarPuzzle(dto.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body("Puzzle creado exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al procesar la imagen", 500));

        } catch (ArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));

        } catch (DataBaseAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error de acceso a base de datos", 500));

        } catch (OperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al crear el puzzle", 500));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error inesperado al crear el puzzle", 500));
        }
    }

    // GET /puzzles/obtenerPuzzles  — todos los puzzles
    @GetMapping("/obtenerPuzzles")
    public ResponseEntity<?> listarPuzzles(@RequestParam(required = false) Puzzle.Estados estado) {
        try {
            List<Puzzle> puzzles = dao.listarPuzzles(estado);

            List<PuzzleDTO> puzzleDTOS = puzzles.stream()
                    .map(p -> PuzzleDTO.fromEntity(p, imageBaseUrl))
                    .toList();

            return ResponseEntity.ok(puzzleDTOS);

        } catch (DataBaseAccessException db) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 409));

        } catch (DataEmptyAccess e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay puzzles registrados", 404));
        }
    }

    // GET /puzzles/misPuzzles?idUsuario=X
    @GetMapping("/misPuzzles")
    public ResponseEntity<?> misPuzzles(@RequestParam Integer idUsuario, @RequestParam(required = false) Puzzle.Estados estado) {
        try {
            List<Puzzle> todos = dao.listarPuzzles(estado);
            List<PuzzleDTO> misPuzzles = todos.stream()
                    .filter(p -> p.getIdUsuario() != null
                            && p.getIdUsuario().getId().equals(idUsuario))
                    .map(p -> PuzzleDTO.fromEntity(p, imageBaseUrl))
                    .toList();

            if (misPuzzles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("No tienes puzzles registrados", 404));
            }
            return ResponseEntity.ok(misPuzzles);

        } catch (DataBaseAccessException db) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 409));

        } catch (DataEmptyAccess e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No hay puzzles registrados", 404));
        }
    }

    @GetMapping("/mejorTiempo")
    public ResponseEntity<?> mejorTiempo() {
        try {
            Integer tiempo = puzzleRepository .obtenerMejorTiempo();
            if (tiempo == null) {
                return ResponseEntity.noContent().build(); // 204: no hay puzzles
            }
            return ResponseEntity.ok(tiempo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener el mejor tiempo", 500));
        }
    }

    // PUT /puzzles/actualizarEstado
    @PutMapping("/actualizarEstado")
    public ResponseEntity<?> actualizarEstado(
            @RequestParam Integer id_usuario,
            @RequestParam Integer id_puzzle,
            @RequestParam Puzzle.Estados tipo) {
        try {
            dao.cambiarEstadoPuzzle(id_usuario, id_puzzle, tipo);
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Error al acceder al puzzle", 409));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));
        }
    }

    // PUT /puzzles/actualizarPuzzle
    @PutMapping("/actualizarPuzzle")
    public ResponseEntity<?> actualizarPuzzle(@RequestBody ActualizarPuzzleDTO dto) {
        try {
            dao.actualizarPuzzle(dto.getIdUsuario(), dto.getIdPuzzle(),
                    dto.getAtributo(), dto.getCambio());
            return ResponseEntity.ok().build();

        } catch (ArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Los datos enviados no son válidos", 400));

        } catch (ObjectNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("El puzzle no existe o no pertenece al usuario", 404));

        } catch (DataEmptyAccess ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El puzzle ya tiene ese valor", 409));

        } catch (DataBaseAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al conectar con la base de datos", 500));

        } catch (OperationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar el puzzle", 500));
        }
    }
}
