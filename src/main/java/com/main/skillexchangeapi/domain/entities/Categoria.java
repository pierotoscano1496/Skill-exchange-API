package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Categoria {
    private final Long id;
    private String nombre;
    private ArrayList<Skill> skills;
}
