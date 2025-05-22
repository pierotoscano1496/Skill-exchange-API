package com.main.skillexchangeapi.app.requests.servicio;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ServicioSkillBody {
    private UUID idServicio;
    private UUID idSkill;
}
