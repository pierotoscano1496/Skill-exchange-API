package com.main.skillexchangeapi.application.services.reviews;

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

    /*@Autowired
    private MongoTemplate mongoTemplate;

     */


    @Override
    public MensajeChat saveMensaje(MensajeChat mensajeChat) {
        return repository.save(mensajeChat);
    }

    @Override
    public List<MensajeChat> obtenerConversacion(UUID idUsuario1, UUID idUsuario2) {
        /*Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("idEmisor").is(idUsuario1).and("idReceptor").is(idUsuario2),
                        Criteria.where("idEmisor").is(idUsuario2).and("idReceptor").is(idUsuario1)
                )
        ).with(Sort.by(Sort.Direction.ASC, "fecha"));

        return mongoTemplate.find(query, MensajeChat.class);

         */
        return repository.findByIdEmisorAndIdReceptorOrIdEmisorAndIdReceptorOrderByFecha(idUsuario1, idUsuario2, idUsuario2, idUsuario1);
    }
}
