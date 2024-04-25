package com.main.skillexchangeapi.apirest.controllers.websockets;

import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
//@RequestMapping(value = "chat")
public class WebSocketChatController {
    @Autowired
    private IChatService service;

    @MessageMapping("/chat/enviar")
    @SendTo("/chatting/save")
    public MensajeChat save(MensajeChat save) {
        return service.saveMensaje(save);
    }

    /*@MessageMapping("/chat/room/{idRoom}")
    @SendTo("/chatting/get/{idRoom}")
    public List<MensajeChat> obtenerMensajesByRoom(@DestinationVariable UUID idRoom) {
        return service.obtenerChatsByRoomId(idRoom);
    }*/
}
