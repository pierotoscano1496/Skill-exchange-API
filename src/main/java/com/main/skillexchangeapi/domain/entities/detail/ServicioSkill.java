package com.main.skillexchangeapi.domain.entities.detail;

import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioSkill {
    Skill skill;
    Servicio servicio;
}
