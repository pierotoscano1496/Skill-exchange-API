package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioSkillResponse {
    private UUID idServicio;
    private UUID idSkill;
}
