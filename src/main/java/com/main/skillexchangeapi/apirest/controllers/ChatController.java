package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.abstractions.services.messaging.IConversationService;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "chat", produces = "application/json")
public class ChatController {
    @Autowired
    private IChatService service;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IConversationService conversationService;

    @GetMapping("/{idConversation}")
    public MensajeChat obtenerChat(@PathVariable UUID idConversation, HttpServletRequest request) {
        String correo = tokenUtils.extractEmailFromRequest(request);

        try {
            UUID idEmisor = usuarioService.obtener(correo).getId();
            MensajeChat mensajeChat = service.obtenerConversacion(idConversation);

            // Verificar participación
            if (mensajeChat.getContacts().stream()
                    .filter(c -> c.getIdContact().equals(idEmisor))
                    .findAny().isPresent()) {
                return service.obtenerConversacion(idConversation);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado a la conversación");
            }
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/own")
    public List<MensajeChat> obtenerConversationsNoMessages(HttpServletRequest request) {
        try {
            return service.obtenerNoMessages(request);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    @GetMapping("/with/{idUsuario}")
    public MensajeChat obtenerConversationWithUser(@PathVariable UUID idUsuario, HttpServletRequest request) {
        try {
            return service.obtenerWithUser(request, idUsuario);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof ResourceNotFoundException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    @GetMapping("/with-no-messages/{idUsuario}")
    public MensajeChat obtenerConversationWithUserNoMessages(@PathVariable UUID idUsuario, HttpServletRequest request) {
        try {
            return service.obtenerWithUserNoMessages(request, idUsuario);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof ResourceNotFoundException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    @PostMapping()
    public MensajeChat enviarPrimerMensaje(@RequestBody FirstMessageChatBody requestBody, HttpServletRequest request) {
        try {
            return service.saveFirstMensaje(request, requestBody);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof ResourceNotFoundException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    // Only for test websocket action
    @PatchMapping("/{idConversation}/send")
    public Message sendMessage(@PathVariable UUID idConversation, @RequestBody MessageBody message) {
        try {
            return service.saveMessageToConversation(idConversation, message);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}
