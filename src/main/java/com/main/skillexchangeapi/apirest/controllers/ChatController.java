package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/with/{idReceptor}")
    public List<MensajeChat> obtenerChat(@PathVariable UUID idReceptor, HttpServletRequest request) {
        String correo = tokenUtils.extractEmailFromRequest(request);
        try {
            UUID idEmisor = usuarioService.obtener(correo).getId();
            return service.obtenerConversacion(idEmisor, idReceptor);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping()
    public MensajeChat enviarPrimerMensaje(@RequestBody FirstMessageChatBody requestBody, HttpServletRequest request) {
        String correo = tokenUtils.extractEmailFromRequest(request);
        try {
            UUID idEmisor = usuarioService.obtener(correo).getId();
            return service.saveMensaje(idEmisor, requestBody);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
