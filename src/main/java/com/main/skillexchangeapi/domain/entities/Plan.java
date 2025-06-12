package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.PlanConstants.Tipo;

@Data
@Builder
public class Plan {
    private final UUID id;
    private String nombre;
    private String codigo;
    private Tipo tipo;
    private boolean isFree;
    private double montoBasico;
}
