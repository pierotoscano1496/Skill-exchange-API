package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.responses.SkillResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioSkillResponse {
    private UUID idServicio;
    private UUID idSkill;
    private SkillResponse skill;
    private ServicioResponse servicio;
}
