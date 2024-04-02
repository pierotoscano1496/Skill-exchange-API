package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlanAsignado {
    private UUID idUsuario;
    private UUID idPlan;
    private boolean isActive;
    private String moneda;
    private double monto;
}
