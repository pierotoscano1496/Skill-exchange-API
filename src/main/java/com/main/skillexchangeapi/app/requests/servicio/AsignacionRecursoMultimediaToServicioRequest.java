package com.main.skillexchangeapi.app.requests.servicio;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants;

import lombok.Getter;

@Getter
public class AsignacionRecursoMultimediaToServicioRequest {
    private String url;
    private RecursoMultimediaContants.Medio medio;
}
