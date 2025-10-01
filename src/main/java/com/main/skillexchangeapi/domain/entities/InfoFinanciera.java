package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InfoFinanciera {
    private final UUID id;
    private Usuario usuario;
    private String clave;
}
