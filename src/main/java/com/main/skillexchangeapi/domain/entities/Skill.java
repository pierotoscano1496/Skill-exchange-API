package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Skill {
    private final UUID id;
    private String nombre;
    private Categoria categoria;
}
