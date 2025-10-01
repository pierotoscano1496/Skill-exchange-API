package com.main.skillexchangeapi.domain.abstractions.repositories.reviews;

import com.main.skillexchangeapi.domain.entities.messaging.InboxItemFlatDto;
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

    @Aggregation(pipeline = {
            "{ $match: { 'contacts.idContact': ?0 } }",
            "{ $set: { " +
                    "    lastMessage: { $cond: [ { $gt: [ { $size: { $ifNull: ['$messages', []] } }, 0 ] }, { $arrayElemAt: ['$messages', -1] }, null ] } "
                    +
                    "} }",
            "{ $set: { _counterpartArr: { $filter: { input: '$contacts', as: 'c', cond: { $ne: ['$$c.idContact', ?0] } } } } }",
            "{ $set: { _counterpart: { $arrayElemAt: ['$_counterpartArr', 0] } } }",
            "{ $project: { " +
                    "    _id: 0, " +
                    "    id: '$_id', " +
                    "    contactId: '$_counterpart.idContact', " +
                    "    contactFullName: '$_counterpart.fullName', " +
                    "    contactEmail: '$_counterpart.email', " +
                    "    lastMessage: 1, " +
                    "    lastDate: '$lastMessage.fecha' " +
                    "} }",
            "{ $sort: { lastDate: -1, id: 1 } }"
    })
    List<InboxItemFlatDto> findInbox(UUID me);
}
