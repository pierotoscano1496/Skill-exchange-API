package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.websockets.MensajeChat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MensajeriaController {

    @MessageMapping("/enviar")
    @SendTo("/chatting/mensajes")
    public MensajeChat enviar(MensajeChat mensajeChat) {
        return new MensajeChat(mensajeChat.nombre(), mensajeChat.contenido());
    }
}
