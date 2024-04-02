package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UsuarioSkillsAsignadosResponse {
    private UUID id;
    private List<SkillAsignado> skillsAsignados;
}
