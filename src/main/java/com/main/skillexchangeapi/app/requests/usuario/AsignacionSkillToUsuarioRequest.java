package com.main.skillexchangeapi.app.requests.usuario;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Builder
public class AsignacionSkillToUsuarioRequest {
    private UUID idSkill; // null para los nuevos skills
    private int nivelConocimiento;
    private String descripcion;
}
