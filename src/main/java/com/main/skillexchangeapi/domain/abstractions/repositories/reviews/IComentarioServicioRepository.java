package com.main.skillexchangeapi.domain.abstractions.repositories.reviews;

import com.main.skillexchangeapi.domain.entities.reviews.ComentarioServicio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface IComentarioServicioRepository extends MongoRepository<ComentarioServicio, UUID> {
    List<ComentarioServicio> findByIdServicio(UUID idServicio);
}
