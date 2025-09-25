package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaResourceUploadedResponse {
    private UUID idRecurso;
    private String url;
    private Medio medio;
}
