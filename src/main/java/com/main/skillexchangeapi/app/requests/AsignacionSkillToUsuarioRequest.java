package com.main.skillexchangeapi.app.requests;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AsignacionSkillToUsuarioRequest {
    private UUID idSkill;
    private int nivelConocimiento;
    private String descripcion;
}
