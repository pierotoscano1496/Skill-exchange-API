package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
public class UsuarioSkillsResponse {
    private UUID idSkill;
    private String descripcionSkill;
    private int nivelConocimiento;
    private String nombreSubCategoria;
    private String nombreCategoria;
    private String descripcion;
}
