package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioSkillResponse {
    private UUID idServicio;
    private UUID idSkill;
    private SkillResponse skill;
    private ServicioResponse servicio;

    public static ServicioSkillResponse fromEntity(ServicioSkill servicioSkill) {
        if (servicioSkill == null) {
            return null;
        }

        Skill skill = servicioSkill.getSkill();
        Servicio servicio = servicioSkill.getServicio();

        return ServicioSkillResponse.builder()
                .idServicio(servicio != null ? servicio.getId() : null)
                .idSkill(skill != null ? skill.getId() : null)
                .skill(SkillResponse.fromEntity(servicioSkill.getSkill()))
                .servicio(ServicioResponse.fromEntity(servicioSkill.getServicio()))
                .build();
    }
}
