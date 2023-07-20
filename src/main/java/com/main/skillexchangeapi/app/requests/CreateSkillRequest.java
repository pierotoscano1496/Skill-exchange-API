package com.main.skillexchangeapi.app.requests;

import com.main.skillexchangeapi.domain.entities.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class CreateSkillRequest {
    private String nombre;
    private UUID idCategoria;
}
