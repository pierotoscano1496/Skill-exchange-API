package com.main.skillexchangeapi.domain.entities.detail;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillInfo {
    private final UUID id;
    private String descripcion;
    private String nombreSubCategoria;
    private String nombreCategoria;
}
