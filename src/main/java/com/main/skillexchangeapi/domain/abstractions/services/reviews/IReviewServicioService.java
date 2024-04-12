package com.main.skillexchangeapi.domain.abstractions.services.reviews;

import com.main.skillexchangeapi.app.responses.reviews.servicio.ServicioReviewResponse;

import java.util.UUID;

public interface IReviewServicioService {
    ServicioReviewResponse review(UUID idServicio);
}
