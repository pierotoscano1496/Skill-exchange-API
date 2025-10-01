package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioImagenResponse {
    private UUID id;
    private UUID idServicio;
    private String urlImagen;
}
