package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;

@Data
@Builder
public class RecursoMultimediaAsignado {
    private UUID id;
    private Medio medio;
    private String url;
}
