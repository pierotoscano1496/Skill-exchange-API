package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
public class PlanResponse {
    private final UUID id;
    private String codigo;
    private String tipo;
    private boolean isFree;
}
