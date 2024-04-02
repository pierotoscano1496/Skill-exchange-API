package com.main.skillexchangeapi.app.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public class RecursosMultimediaAsignadoToServicioResponse {
    private UUID id;
    private String url;
    private String medio;
    private UUID idServicio;
}
