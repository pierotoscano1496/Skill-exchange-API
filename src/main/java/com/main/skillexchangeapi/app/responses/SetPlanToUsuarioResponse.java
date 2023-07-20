package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
public class SetPlanToUsuarioResponse {
    private UUID idPlan;
    private boolean isActive = true;
    private double monto;
    private String moneda;
}
