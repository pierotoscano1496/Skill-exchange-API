package com.main.skillexchangeapi.app.requests;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SetPlanToUsuarioRequest {
    private UUID idPlan;
    private boolean isActive = true;
    private double monto;
    private String moneda;
}
