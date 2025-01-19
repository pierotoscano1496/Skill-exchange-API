package com.main.skillexchangeapi.domain.entities.reviews;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document
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
