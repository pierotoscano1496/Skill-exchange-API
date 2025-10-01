package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.app.responses.reviews.servicio.ServicioReviewResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.reviews.IComentarioServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IReviewServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewServicioService implements IReviewServicioService {
    @Autowired
    private IComentarioServicioRepository comentarioServicioRepository;

    @Override
    public ServicioReviewResponse review(UUID idServicio) {
        List<ComentarioServicioResponse> comentariosServicio = comentarioServicioRepository.findByIdServicio(idServicio)
                .stream()
                .map(c -> ComentarioServicioResponse.builder()
                        .id(c.getId())
                        .idServicio(c.getIdServicio())
                        .idComentarista(c.getIdComentarista())
                        .nombresComentarista(c.getNombresComentarista())
                        .apellidosComentarista(c.getApellidosComentarista())
                        .puntaje(c.getPuntaje())
                        .comentario(c.getComentario())
                        .build()).collect(Collectors.toList());

        OptionalDouble puntajeProm = comentariosServicio
                .stream().mapToInt(c -> c.getPuntaje()).average();

        return ServicioReviewResponse.builder()
                .comentarios(comentariosServicio)
                .puntajePromedio(puntajeProm.isPresent()
                        ? (float) (Math.round(puntajeProm.getAsDouble() * 10.0) / 10.0)
                        : 0)
                .build();
    }
}
