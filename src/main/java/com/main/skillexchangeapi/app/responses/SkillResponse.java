package com.main.skillexchangeapi.app.responses;

import com.main.skillexchangeapi.domain.entities.Categoria;
import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
public class SkillResponse {
    private UUID id;
    private String descripcion;
    private UUID idCcategoria;
}
