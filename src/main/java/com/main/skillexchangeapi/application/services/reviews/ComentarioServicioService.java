package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.requests.review.comentarios.ComentarioServicioBody;
import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.reviews.IComentarioServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IComentarioServicioService;
import com.main.skillexchangeapi.domain.entities.reviews.ComentarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComentarioServicioService implements IComentarioServicioService {
    @Autowired
    private IComentarioServicioRepository repository;

    @Override
    public List<ComentarioServicioResponse> obtenerByServicio(UUID idServicio) {
        return repository.findByIdServicio(idServicio).stream().map(c -> ComentarioServicioResponse.builder()
                .id(c.getId())
                .idServicio(c.getIdServicio())
                .idComentarista(c.getIdComentarista())
                .nombresComentarista(c.getNombresComentarista())
                .apellidosComentarista(c.getApellidosComentarista())
                .puntaje(c.getPuntaje())
                .comentario(c.getComentario())
                .build()).collect(Collectors.toList());
    }

    @Override
    public ComentarioServicioResponse publicar(ComentarioServicioBody requestBody) {
        ComentarioServicio comentarioSaved = repository.save(ComentarioServicio.builder()
                .id(UuidManager.randomUuid())
                .idServicio(requestBody.getIdServicio())
                .comentario(requestBody.getComentario())
                .puntaje(requestBody.getPuntaje())
                .idComentarista(requestBody.getIdComentarista())
                .nombresComentarista(requestBody.getNombresComentarista())
                .apellidosComentarista(requestBody.getApellidosComentarista())
                .build());

        return ComentarioServicioResponse.builder()
                .idServicio(comentarioSaved.getIdServicio())
                .id(comentarioSaved.getId())
                .comentario(comentarioSaved.getComentario())
                .idComentarista(comentarioSaved.getIdComentarista())
                .nombresComentarista(comentarioSaved.getNombresComentarista())
                .apellidosComentarista(comentarioSaved.getApellidosComentarista())
                .puntaje(comentarioSaved.getPuntaje())
                .build();
    }
}
