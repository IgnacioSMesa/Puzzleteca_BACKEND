package com.ignacio_natalia.api.controlador;

import com.ignacio_natalia.api.dto.ChatDTO.MensajeEntranteDTO;
import com.ignacio_natalia.api.dto.ChatDTO.MensajeSalienteDTO;
import com.ignacio_natalia.api.modelo.Mensaje;
import com.ignacio_natalia.api.modelo.Usuario;
import com.ignacio_natalia.api.repositorio.ConversacionRepository;
import com.ignacio_natalia.api.repositorio.MensajeRepository;
import com.ignacio_natalia.api.repositorio.ParticipantesRepository;
import com.ignacio_natalia.api.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private MensajeRepository mensajeRepo;

    @Autowired
    private ConversacionRepository conversacionRepo;

    @Autowired
    private ParticipantesRepository participantesRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.enviar")
    public void enviarMensaje(@Payload MensajeEntranteDTO dto) {

        if (dto.getIdUsuario() == null) return;
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(dto.getIdUsuario());

        Usuario usuario = usuarioOpt.get();

        boolean esParticipante = participantesRepo
                .existsByIdConversationIdAndIdUsuarioId(dto.getIdConversacion(), usuario.getId());

        if (!esParticipante) return;

        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(dto.getContenido());
        mensaje.setIdConversation(conversacionRepo.getReferenceById(dto.getIdConversacion()));
        mensaje.setIdUsuario(usuario);
        mensaje.setCreadoEn(OffsetDateTime.now());
        mensajeRepo.save(mensaje);

        MensajeSalienteDTO saliente = new MensajeSalienteDTO();
        saliente.setIdMensaje(mensaje.getId());
        saliente.setIdConversacion(dto.getIdConversacion());
        saliente.setIdUsuario(usuario.getId());
        saliente.setNombre(usuario.getNombre());
        saliente.setContenido(mensaje.getContenido());
        saliente.setCreadoEn(mensaje.getCreadoEn());

        messagingTemplate.convertAndSend(
                "/topic/conversacion/" + dto.getIdConversacion(), saliente
        );

    }
}