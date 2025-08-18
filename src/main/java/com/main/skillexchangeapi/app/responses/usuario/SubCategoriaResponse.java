package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.responses.SkillResponse;

@Data
@Builder
public class SubCategoriaResponse {
    private UUID id;
    private String nombre;
    private UUID idCategoria;
    private CategoriaResponse categoria;
    private List<SkillResponse> skills;
}
