package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class ComentarioServicio {
    private final Long id;
    private Usuario comentarista;
    private String comentario;
    private int likes;
    private int dislikes;
    private ComentarioServicio respuesta;
}
