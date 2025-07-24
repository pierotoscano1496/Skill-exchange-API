package com.main.skillexchangeapi.app.responses.skill;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillInfoResponse {
    private UUID id;
    private String descripcion;
    private String nombreSubCategoria;
    private String nombreCategoria;
}
