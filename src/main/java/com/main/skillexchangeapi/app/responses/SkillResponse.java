package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;

@Data
@Builder
public class SkillResponse {
    private UUID id;
    private String descripcion;
    private UUID idSubCategoria;
    private SubCategoriaResponse subCategoria;
}
