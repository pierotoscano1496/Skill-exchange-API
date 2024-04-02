package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SubCategoriaResponse {
    private UUID id;
    private String nombre;
    private UUID idCategoria;
}
