package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecursoMultimediaAsignado {
    private UUID id;
    private String medio;
    private String url;
}
