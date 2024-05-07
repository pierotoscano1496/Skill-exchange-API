package com.main.skillexchangeapi.domain.abstractions.repositories.reviews;

import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IChatRepository extends MongoRepository<MensajeChat, UUID> {
    @Query("{$and: [{'contacts.idContact': ?0}, {'contacts.idContact': ?1}]}")
    Optional<MensajeChat> findByIdContacts(UUID idContact1, UUID idContact2);
}
