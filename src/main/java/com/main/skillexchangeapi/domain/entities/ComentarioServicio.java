package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ComentarioServicio {
    private final UUID id;
    private Usuario comentarista;
    private String comentario;
    private int likes;
    private int dislikes;
    private ComentarioServicio respuesta;
}
