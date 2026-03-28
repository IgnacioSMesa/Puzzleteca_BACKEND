package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.ChatDTO.CrearConversacionDTO;
import com.ignacio_natalia.api.dto.ChatDTO.MensajeHistorialDTO;
import com.ignacio_natalia.api.modelo.Conversacion;
import com.ignacio_natalia.api.modelo.Mensaje;
import com.ignacio_natalia.api.modelo.ParticipantesConversacion;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    @Autowired
    private ConversacionRepository conversacionRepo;

    @Autowired
    private ParticipantesRepository participantesRepo;

    @Autowired
    private MensajeRepository mensajeRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @PostMapping("/crearConversacion")
    public ResponseEntity<?> crearConversacion(@RequestBody CrearConversacionDTO dto) {

        // Buscar los usuarios por email
        List<Usuario> usuarios = dto.getParticipantes().stream()
                .map(email -> usuarioRepo.findUsuarioByEmail(email).orElse(null))
                .filter(u -> u != null)
                .toList();

        if (usuarios.size() < 2) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Participantes no válidos", 400));
        }

        List<Integer> ids = usuarios.stream().map(Usuario::getId).toList();

        // Buscar si ya existe una conversación entre estos dos usuarios
        List<Long> existentes = participantesRepo.findConversacionByParticipantes(ids, ids.size());

        if (!existentes.isEmpty()) {
            return ResponseEntity.ok(Map.of("idConversacion", existentes.get(0)));
        }

        // Crear nueva conversación
        Conversacion conv = new Conversacion();
        conv.setCreadoEn(OffsetDateTime.now());
        conv.setActualizadoEn(OffsetDateTime.now());
        conversacionRepo.save(conv);

        // Añadir participantes
        for (Usuario u : usuarios) {
            ParticipantesConversacion p = new ParticipantesConversacion();
            p.setIdConversation(conv);
            p.setIdUsuario(u);
            participantesRepo.save(p);
        }

        return ResponseEntity.ok(Map.of("idConversacion", conv.getId()));
    }

    @GetMapping("/mensajes/{idConversacion}")
    public ResponseEntity<?> obtenerMensajes(@PathVariable Long idConversacion) {

        List<Mensaje> mensajes = mensajeRepo.findByIdConversationIdOrderByCreadoEnAsc(idConversacion);

        List<MensajeHistorialDTO> dtos = mensajes.stream()
                .map(MensajeHistorialDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
