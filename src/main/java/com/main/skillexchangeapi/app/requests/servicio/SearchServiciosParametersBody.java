package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SearchServiciosParametersBody {
    private String keyWord;
    private UUID idSkill;
    private UUID idSubcategoria;
    private UUID idCategoria;
}
