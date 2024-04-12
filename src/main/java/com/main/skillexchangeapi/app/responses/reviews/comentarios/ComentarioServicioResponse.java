package com.main.skillexchangeapi.app.responses.reviews.comentarios;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ComentarioServicioResponse {
    private UUID id;
    private UUID idServicio;
    private UUID idComentarista;
    private String nombresComentarista;
    private String apellidosComentarista;
    private String comentario;
    private int puntaje;
}
