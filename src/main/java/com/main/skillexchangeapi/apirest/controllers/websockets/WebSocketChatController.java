package com.main.skillexchangeapi.apirest.controllers.websockets;

import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
//@RequestMapping(value = "chat")
public class WebSocketChatController {
    @Autowired
    private IChatService service;

    @MessageMapping("/chat/{idConversation}/send")
    @SendTo("/chatting/{idConversation}")
    public Message save(@DestinationVariable UUID idConversation, MessageBody message) {
        try {
            return service.saveMessageToConversation(idConversation, message);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    /*@MessageMapping("/chat/enviar")
    @SendTo("/chatting/save")
    public MensajeChat save(MensajeChat save) {
        return service.saveMensaje(save);
    }
     */

    /*@MessageMapping("/chat/room/{idRoom}")
    @SendTo("/chatting/get/{idRoom}")
    public List<MensajeChat> obtenerMensajesByRoom(@DestinationVariable UUID idRoom) {
        return service.obtenerChatsByRoomId(idRoom);
    }*/
}
