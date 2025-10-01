package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServicioRecursosMultimediaAsignadosResponse {
    private UUID id;
    private List<RecursoMultimediaAsignado> recursosMultimediaAsignados;
}
