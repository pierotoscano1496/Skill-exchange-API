package com.main.skillexchangeapi.domain.abstractions.repositories.messaging;

import com.main.skillexchangeapi.domain.entities.messaging.Conversation;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IConversationRepository {
    Conversation registrar(Conversation conversation) throws DatabaseNotWorkingException, NotCreatedException;

    List<Conversation> obtenerFromUser(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;

    Conversation obtenerFromUsers(UUID idUsuario1, UUID idUsuario2) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
