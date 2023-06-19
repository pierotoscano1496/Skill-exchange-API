package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class RecursoMultimediaServicio {
    private final Long id;
    private Servicio servicio;
    private String url;
    private String medio;
}
