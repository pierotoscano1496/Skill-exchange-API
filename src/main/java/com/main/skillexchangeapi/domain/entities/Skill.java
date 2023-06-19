package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class Skill {
    private final Long id;
    private String nombre;
    private Categoria categoria;
}
