package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.domain.entities.Servicio;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecursoMultimediaResponse {
    private final UUID id;
    private Servicio servicio;
    private String url;
    private String medio;
}
