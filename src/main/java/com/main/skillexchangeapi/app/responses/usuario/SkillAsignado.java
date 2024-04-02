package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SkillAsignado {
    private UUID id;
    private int nivelConocimiento;
    private String descripcion;
}
