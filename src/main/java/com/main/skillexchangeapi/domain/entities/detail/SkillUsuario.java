package com.main.skillexchangeapi.domain.entities.detail;

import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SkillUsuario {
    private Usuario usuario;
    private Skill skill;
    private int nivelConocimiento;
    private String descripcion;
}
