package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.app.responses.messaging.ChatWithLastMessageResponse;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
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

    @GetMapping("/{idConversation}")
    public ConversationResponse getConversationById(@PathVariable UUID idConversation, HttpServletRequest request) {
        String correo = tokenUtils.extractEmailFromRequest(request);

        try {
            UUID idEmisor = usuarioService.obtener(correo).getId();
            return service.getConversationById(idConversation, idEmisor);
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

    @GetMapping("/own-last-message")
    public List<ChatWithLastMessageResponse> obtenerConversacionesConUltimoMensaje(HttpServletRequest request) {
        try {
            return service.obtenerChatsWithLasMessage(request);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
