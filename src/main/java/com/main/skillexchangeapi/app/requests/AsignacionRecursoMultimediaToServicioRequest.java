package com.main.skillexchangeapi.app.requests;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AsignacionRecursoMultimediaToServicioRequest {
    private String url;
    private String medio;
    private UUID idServicio;
}
