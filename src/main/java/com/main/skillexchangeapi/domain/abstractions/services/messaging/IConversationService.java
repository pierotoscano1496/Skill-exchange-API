package com.main.skillexchangeapi.domain.abstractions.services.messaging;

import com.main.skillexchangeapi.app.requests.messaging.CreateConversationBody;
import com.main.skillexchangeapi.app.responses.messaging.ConversationResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface IConversationService {
    ConversationResponse registrar(CreateConversationBody requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    List<ConversationResponse> obtenerOwn(HttpServletRequest request) throws DatabaseNotWorkingException, ResourceNotFoundException;

    ConversationResponse obtenerWithUser(HttpServletRequest request, UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
