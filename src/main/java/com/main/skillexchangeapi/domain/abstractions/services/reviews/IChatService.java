package com.main.skillexchangeapi.domain.abstractions.services.reviews;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.requests.messaging.MessageBody;
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

    MensajeChat saveFirstMensaje(HttpServletRequest request, FirstMessageChatBody requestBody) throws DatabaseNotWorkingException, ResourceNotFoundException;

    Message saveMessageToConversation(UUID id, MessageBody message) throws ResourceNotFoundException;

    MensajeChat obtenerConversacion(UUID idRoom) throws ResourceNotFoundException;

    MensajeChat obtenerWithUser(HttpServletRequest request, UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;
    MensajeChat obtenerWithUserNoMessages(HttpServletRequest request, UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
