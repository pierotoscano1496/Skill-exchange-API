package com.main.skillexchangeapi.domain.entities.reviews;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
public class ComentarioServicio {
    @Id
    private UUID id;
    private UUID idServicio;
    private UUID idComentarista;
    private String nombresComentarista;
    private String apellidosComentarista;
    private String comentario;
    private int puntaje;
}
