package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.messaging.IConversationRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.reviews.IChatRepository;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.messaging.Contact;
import com.main.skillexchangeapi.domain.entities.messaging.Conversation;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChatProjection;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService implements IChatService {
    @Autowired
    private IChatRepository repository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IConversationRepository conversationRepository;

    @Autowired
    private TokenUtils tokenUtils;

    public MensajeChat saveMensaje(MensajeChat mensajeChat) {
        mensajeChat.setId(UuidManager.randomUuid());
        return repository.save(mensajeChat);
    }

    @Override
    public MensajeChat saveFirstMensaje(HttpServletRequest request, FirstMessageChatBody requestBody)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idEmisor = usuarioRepository.obtenerByCorreo(correo).getId();

        // Evaluar si hay una conversación previa con el receptor
        Optional<MensajeChat> previousConversation = repository.findByIdContacts(idEmisor, requestBody.getIdReceptor());
        if (previousConversation.isPresent()) {
            // Enviar mensaje a la conversación existente
            MensajeChat mensajeChat = previousConversation.get();
            Message newMessage = Message.builder()
                    .sentBy(idEmisor)
                    .mensaje(requestBody.getMensaje())
                    .resourceUrl(requestBody.getResourceUrl())
                    .fecha(new Date())
                    .build();

            mensajeChat.getMessages().add(newMessage);
            return repository.save(mensajeChat);
        } else {
            // Crear nueva conversación
            // Contactos
            Usuario emisor = usuarioRepository.obtenerById(idEmisor);
            Usuario receptor = usuarioRepository.obtenerById(requestBody.getIdReceptor());

            Contact contactEmisor = Contact.builder()
                    .idContact(emisor.getId())
                    .fullName(emisor.getNombres() + " " + emisor.getApellidos())
                    .email(emisor.getCorreo())
                    .build();

            Contact contactReceptor = Contact.builder()
                    .idContact(receptor.getId())
                    .fullName(receptor.getNombres() + " " + receptor.getApellidos())
                    .email(receptor.getCorreo())
                    .build();

            Message firstMessage = Message.builder()
                    .sentBy(emisor.getId())
                    .fecha(new Date())
                    .mensaje(requestBody.getMensaje())
                    .resourceUrl(requestBody.getResourceUrl())
                    .build();

            MensajeChat mensajeChat = MensajeChat.builder()
                    .id(UuidManager.randomUuid())
                    .contacts(Arrays.asList(contactEmisor, contactReceptor))
                    .messages(Arrays.asList(firstMessage))
                    .build();

            return repository.save(mensajeChat);
        }
    }

    @Override
    public Message saveMessageToConversation(UUID id, MessageBody message) throws ResourceNotFoundException {
        Optional<MensajeChat> mensajeChatFound = repository.findById(id);
        if (mensajeChatFound.isPresent()) {
            MensajeChat mensajeChat = mensajeChatFound.get();
            Message newMessage = Message.builder()
                    .sentBy(message.getSentBy())
                    .mensaje(message.getMensaje())
                    .resourceUrl(message.getResourceUrl())
                    .fecha(new Date())
                    .build();

            mensajeChat.getMessages().add(newMessage);
            repository.save(mensajeChat);
            return newMessage;
        } else {
            throw new ResourceNotFoundException("No existe la conversación");
        }

    }

    @Override
    public MensajeChat obtenerConversacion(UUID id) throws ResourceNotFoundException {
        Optional<MensajeChat> mensajeChatFound = repository.findById(id);
        if (mensajeChatFound.isPresent()) {
            return mensajeChatFound.get();
        } else {
            throw new ResourceNotFoundException("No existe la conversación");
        }
    }

    @Override
    public List<MensajeChat> obtenerNoMessages(HttpServletRequest request)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idUsuarioLogged = usuarioRepository.obtenerByCorreo(correo).getId();
        return repository.findByIdContactExcludeMessages(idUsuarioLogged);
    }

    @Override
    public MensajeChat obtenerWithUser(HttpServletRequest request, UUID idUsuario)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idEmisor = usuarioRepository.obtenerByCorreo(correo).getId();

        // Optional<MensajeChat> mensajeChat =
        // repository.findByIdContacts(UuidManager.UuidToBytes(idEmisor),
        // UuidManager.UuidToBytes(idUsuario));
        Optional<MensajeChat> mensajeChat = repository.findByIdContacts(idEmisor, idUsuario);
        if (mensajeChat.isPresent()) {
            return mensajeChat.get();
        } else {
            throw new ResourceNotFoundException("No existe la conversación");
        }
    }

    @Override
    public MensajeChat obtenerWithUserNoMessages(HttpServletRequest request, UUID idUsuario)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idEmisor = usuarioRepository.obtenerByCorreo(correo).getId();

        // Optional<MensajeChat> mensajeChat =
        // repository.findByIdContacts(UuidManager.UuidToBytes(idEmisor),
        // UuidManager.UuidToBytes(idUsuario));
        Optional<MensajeChat> mensajeChat = repository.findByIdContactExcludeMessages(idEmisor, idUsuario);
        if (mensajeChat.isPresent()) {
            return mensajeChat.get();
        } else {
            throw new ResourceNotFoundException("No existe la conversación");
        }
    }

    @Override
    public List<MensajeChatProjection> obtenerChatsWithLasMessage(HttpServletRequest request)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idUsuarioLogged = usuarioRepository.obtenerByCorreo(correo).getId();
        return repository.findChatsWithLastMessage(idUsuarioLogged);
    }
}
