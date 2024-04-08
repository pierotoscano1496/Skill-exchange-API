package com.main.skillexchangeapi.domain.entities.searchparameters;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchServicioParams {
    private String keyWord;
    private UUID idSkill;
    private UUID idSubcategoria;
    private UUID idCategoria;
}
