package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaResourceUploadedResponse {
    private String url;
    private Medio medio;
}
