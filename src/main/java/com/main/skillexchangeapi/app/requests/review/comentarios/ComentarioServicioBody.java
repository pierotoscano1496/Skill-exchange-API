package com.main.skillexchangeapi.app.requests.review.comentarios;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ComentarioServicioBody {
    private UUID id;
    private UUID idServicio;
    private UUID idComentarista;
    private String nombresComentarista;
    private String apellidosComentarista;
    private String comentario;
    private int puntaje;
}
