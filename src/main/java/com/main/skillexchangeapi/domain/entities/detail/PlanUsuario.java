package com.main.skillexchangeapi.domain.entities.detail;

import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Usuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanUsuario {
    private Plan plan;
    private Usuario usuario;
    private double monto;
    private String moneda;
    private boolean isActive;
}
