package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
public class Categoria {
    private final UUID id;
    private String nombre;
    private ArrayList<SubCategoria> subCategorias;
}
