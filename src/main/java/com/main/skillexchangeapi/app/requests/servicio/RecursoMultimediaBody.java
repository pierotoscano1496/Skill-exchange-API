package com.main.skillexchangeapi.app.requests.servicio;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants;

import lombok.Getter;

@Getter
public class RecursoMultimediaBody {
    private RecursoMultimediaContants.Medio medio;
    private String url;
}
