package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SkillResponse {
    private UUID id;
    private String descripcion;
    private UUID idSubCategoria;
}
