package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class Plan {
    private final Long id;
    private String codigo;
    private String tipo;
    private boolean isFree;
}
