package com.main.skillexchangeapi.domain.abstractions.repositories.reviews;

import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface IChatRepository extends MongoRepository<MensajeChat, UUID> {
    List<MensajeChat> findByIdEmisorAndIdReceptorOrIdEmisorAndIdReceptorOrderByFecha(UUID idEmisor1, UUID idReceptor1, UUID idEmisor2, UUID idReceptor2);
}
