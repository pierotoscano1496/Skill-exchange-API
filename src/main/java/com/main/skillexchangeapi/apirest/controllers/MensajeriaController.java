package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.websockets.MensajeChat;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MensajeriaController {

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/mensajes/{roomId}")
    public MensajeChat enviar(@DestinationVariable String roomId, MensajeChat mensajeChat) {
        return new MensajeChat(mensajeChat.getMessage(),mensajeChat.getUser());
    }
}
