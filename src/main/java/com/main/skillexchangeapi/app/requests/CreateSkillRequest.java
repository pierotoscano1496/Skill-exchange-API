package com.main.skillexchangeapi.app.requests;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateSkillRequest {
    private String descripcion;
    private UUID idSubcategoria;
}
