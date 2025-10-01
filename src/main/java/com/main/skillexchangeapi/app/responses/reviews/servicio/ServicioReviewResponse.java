package com.main.skillexchangeapi.app.responses.reviews.servicio;

import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServicioReviewResponse {
    List<ComentarioServicioResponse> comentarios;
    float puntajePromedio;
}
