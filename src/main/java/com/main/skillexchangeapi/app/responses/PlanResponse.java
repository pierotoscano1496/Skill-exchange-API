package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.PlanConstants.Tipo;

@Setter
@Builder
public class PlanResponse {
    private final UUID id;
    private String codigo;
    private Tipo tipo;
    private boolean isFree;
}
