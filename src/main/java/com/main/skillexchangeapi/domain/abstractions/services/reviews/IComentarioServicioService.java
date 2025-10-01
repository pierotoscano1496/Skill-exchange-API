package com.main.skillexchangeapi.domain.abstractions.services.reviews;

import com.main.skillexchangeapi.app.requests.review.comentarios.ComentarioServicioBody;
import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;

import java.util.List;
import java.util.UUID;

public interface IComentarioServicioService {
    List<ComentarioServicioResponse> obtenerByServicio(UUID idServicio);

    ComentarioServicioResponse publicar(ComentarioServicioBody requestBody);
}
