package com.main.skillexchangeapi.domain.abstractions.repositories.reviews;

import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;
import com.main.skillexchangeapi.domain.entities.messaging.MensajeChatProjection;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChatRepository extends MongoRepository<MensajeChat, UUID> {
    @Query(value = "{'contacts.idContact': ?0}", fields = "{messages: 0}")
    List<MensajeChat> findByIdContactExcludeMessages(UUID idContact);

    @Query("{$and: [{'contacts.idContact': ?0}, {'contacts.idContact': ?1}]}")
    Optional<MensajeChat> findByIdContacts(UUID idContact1, UUID idContact2);

    @Query(value = "{$and: [{'contacts.idContact': ?0}, {'contacts.idContact': ?1}]}", fields = "{messages: 0}")
    Optional<MensajeChat> findByIdContactExcludeMessages(UUID idContact1, UUID idContact2);

    @Aggregation(pipeline = {
            " { '$match': { 'contacts.idContact': ?0 } }",
            " { '$project': { 'id': '$_id', 'contacts': 1, 'lastMessage': { $arrayElemAt: ['$messages', -1] } } }"
    })
    List<MensajeChatProjection> findChatsWithLastMessage(UUID idContact);
}
