package com.main.skillexchangeapi.domain.abstractions.services.reviews;

import com.main.skillexchangeapi.app.responses.messaging.ChatWithLastMessageResponse;
import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;

import com.main.skillexchangeapi.domain.entities.messaging.Message;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChatService {
        MensajeChat saveMensaje(MensajeChat mensajeChat);

        MensajeChat saveFirstMensaje(HttpServletRequest request, FirstMessageChatBody requestBody)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        Message saveMessageToConversation(UUID id, MessageBody message) throws ResourceNotFoundException;

        ConversationResponse getConversationById(UUID idConversation, UUID idLoggedUser)
                        throws ResourceNotFoundException;

        List<MensajeChat> obtenerNoMessages(HttpServletRequest request)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<ChatWithLastMessageResponse> obtenerChatsWithLasMessage(HttpServletRequest request)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;
}
