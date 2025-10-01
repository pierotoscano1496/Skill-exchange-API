package com.main.skillexchangeapi.apirest.controllers.websockets;

import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
// @RequestMapping(value = "chat")
public class WebSocketChatController {
    @Autowired
    private IChatService service;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{idConversation}/send")
    public void save(@DestinationVariable UUID idConversation, @Payload MessageBody message, Principal principal)
            throws ResourceNotFoundException {
        Message messageSent = service.saveMessageToConversation(idConversation, message);
        messagingTemplate.convertAndSend("/chatting/" + idConversation, messageSent);
    }

    @MessageExceptionHandler(ResourceNotFoundException.class)
    public void handleNotFound(ResourceNotFoundException exception, Principal principal) {
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/errors", exception.getMessage());
    }

    /*
     * @MessageMapping("/chat/enviar")
     * 
     * @SendTo("/chatting/save")
     * public MensajeChat save(MensajeChat save) {
     * return service.saveMensaje(save);
     * }
     */

    /*
     * @MessageMapping("/chat/room/{idRoom}")
     * 
     * @SendTo("/chatting/get/{idRoom}")
     * public List<MensajeChat> obtenerMensajesByRoom(@DestinationVariable UUID
     * idRoom) {
     * return service.obtenerChatsByRoomId(idRoom);
     * }
     */
}
