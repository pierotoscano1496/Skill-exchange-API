package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.app.responses.messaging.ChatWithLastMessageResponse;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
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
        UUID idEmisor = tokenUtils.extractIdFromRequest(request);

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
    public ConversationResponse getConversationById(UUID idConversation, UUID idLoggedUser)
            throws ResourceNotFoundException {
        Optional<MensajeChat> mensajeChatFound = repository.findById(idConversation);
        if (mensajeChatFound.isPresent()) {
            MensajeChat mensajeChat = mensajeChatFound.get();
            Contact otherContact = null;
            for (Contact contact : mensajeChat.getContacts()) {
                if (!contact.getIdContact().equals(idLoggedUser)) {
                    otherContact = contact;
                    break;
                }
            }
            return ConversationResponse.builder()
                    .conversationId(mensajeChat.getId())
                    .otherContact(otherContact)
                    .messages(mensajeChat.getMessages())
                    .build();
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
    public List<ChatWithLastMessageResponse> obtenerChatsWithLasMessage(HttpServletRequest request)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        String correo = tokenUtils.extractEmailFromRequest(request);
        UUID idUsuarioLogged = usuarioRepository.obtenerByCorreo(correo).getId();
        List<MensajeChatProjection> projections = repository.findChatsWithLastMessage(idUsuarioLogged);
        List<ChatWithLastMessageResponse> response = new ArrayList<>();

        for (MensajeChatProjection projection : projections) {
            Contact otherContact = null;
            for (Contact contact : projection.getContacts()) {
                if (!contact.getIdContact().equals(idUsuarioLogged)) {
                    otherContact = contact;
                    break;
                }
            }

            response.add(
                    ChatWithLastMessageResponse.builder()
                            .conversationId(projection.getId())
                            .contact(otherContact)
                            .lastMessage(projection.getLastMessage())
                            .build());
        }
        return response;
    }
}
