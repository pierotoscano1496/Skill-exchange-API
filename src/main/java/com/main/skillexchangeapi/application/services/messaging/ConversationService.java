package com.main.skillexchangeapi.application.services.messaging;

import com.main.skillexchangeapi.app.requests.messaging.CreateConversationBody;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.messaging.IConversationService;
import com.main.skillexchangeapi.domain.entities.messaging.Conversation;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.repositories.UsuarioRepository;
import com.main.skillexchangeapi.infraestructure.repositories.messaging.ConversationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationService implements IConversationService {
    @Autowired
    private ConversationRepository repository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ConversationResponse registrar(CreateConversationBody requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        Conversation conversationRegistered = repository.registrar(Conversation.builder()
                .id(UuidManager.randomUuid())
                .idEmisor(requestBody.getIdEmisor())
                .idReceptor(requestBody.getIdReceptor())
                .build());

        return ConversationResponse.builder()
                .id(conversationRegistered.getId())
                .idEmisor(conversationRegistered.getIdEmisor())
                .idReceptor(conversationRegistered.getIdReceptor())
                .build();
    }

    @Override
    public List<ConversationResponse> obtenerOwn(HttpServletRequest request) throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idUsuario = usuarioRepository.obtenerByCorreo(correo).getId();

        return repository.obtenerFromUser(idUsuario)
                .stream().map(c -> ConversationResponse.builder()
                        .id(c.getId())
                        .idEmisor(c.getIdEmisor())
                        .idReceptor(c.getIdReceptor())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public ConversationResponse obtenerWithUser(HttpServletRequest request, UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idUsuarioLogged = usuarioRepository.obtenerByCorreo(correo).getId();

        Conversation conversation = repository.obtenerFromUsers(idUsuarioLogged, idUsuario);

        return ConversationResponse.builder()
                .id(conversation.getId())
                .idEmisor(conversation.getIdEmisor())
                .idReceptor(conversation.getIdReceptor())
                .build();
    }
}
