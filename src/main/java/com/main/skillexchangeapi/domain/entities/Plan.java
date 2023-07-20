package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Plan {
    private final UUID id;
    private String codigo;
    private String tipo;
    private boolean isFree;
}
