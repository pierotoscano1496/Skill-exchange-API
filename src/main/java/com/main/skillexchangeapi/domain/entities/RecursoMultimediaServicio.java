package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;

@Data
@Builder
public class RecursoMultimediaServicio {
    private final UUID id;
    private Servicio servicio;
    private String url;
    private Medio medio;
}
