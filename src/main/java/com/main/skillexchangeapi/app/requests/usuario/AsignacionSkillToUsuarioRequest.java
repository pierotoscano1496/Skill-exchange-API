package com.main.skillexchangeapi.app.requests.usuario;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AsignacionSkillToUsuarioRequest {
    private UUID idSkill; // null para los nuevos skills
    private int nivelConocimiento;
    private String descripcion;
}
