package com.main.skillexchangeapi.app.responses;

import lombok.Builder;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;

@Builder
public class RecursosMultimediaAsignadoToServicioResponse {
    private UUID id;
    private String url;
    private Medio medio;
    private UUID idServicio;
}
