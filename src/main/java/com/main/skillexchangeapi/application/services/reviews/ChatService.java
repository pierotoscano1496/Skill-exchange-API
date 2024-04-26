package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.requests.messaging.FirstMessageChatBody;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.reviews.IChatRepository;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IChatService;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService implements IChatService {
    @Autowired
    private IChatRepository repository;

    @Override
    public MensajeChat saveMensaje(MensajeChat mensajeChat) {
        return repository.save(mensajeChat);
    }

    @Override
    public MensajeChat saveMensaje(UUID idEmisor, FirstMessageChatBody requestBody) {
        MensajeChat mensajeChat = MensajeChat.builder()
                .id(UuidManager.randomUuid())
                .idEmisor(idEmisor)
                .idReceptor(requestBody.getIdReceptor())
                .mensaje(requestBody.getMensaje())
                .fecha(requestBody.getFecha())
                .resourceUrl(requestBody.getResourceUrl())
                .build();

        return repository.save(mensajeChat);
    }

    @Override
    public List<MensajeChat> obtenerConversacion(UUID idUsuario1, UUID idUsuario2) {
        return repository.findByIdEmisorAndIdReceptorOrIdEmisorAndIdReceptorOrderByFecha(idUsuario1, idUsuario2, idUsuario2, idUsuario1);
    }
}
