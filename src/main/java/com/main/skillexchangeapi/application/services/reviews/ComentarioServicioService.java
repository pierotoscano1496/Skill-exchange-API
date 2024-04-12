package com.main.skillexchangeapi.application.services.reviews;

import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.reviews.IComentarioServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IComentarioServicioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ComentarioServicioResponse publicar() {
        return null;
    }
}
