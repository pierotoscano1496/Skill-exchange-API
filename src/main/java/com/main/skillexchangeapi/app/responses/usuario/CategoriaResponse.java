package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CategoriaResponse {
    private UUID id;
    private String nombre;
    private List<SubCategoriaResponse> subCategorias;
}
