package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.domain.entities.Categoria;

@Data
@Builder
public class CategoriaResponse {
    private UUID id;
    private String nombre;
    private List<SubCategoriaResponse> subCategorias;

    public static CategoriaResponse fromEntity(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        List<SubCategoriaResponse> subCategorias = null;

        if (categoria.getSubCategorias() != null && !categoria.getSubCategorias().isEmpty()) {
            subCategorias = categoria.getSubCategorias().stream()
                    .map(SubCategoriaResponse::fromEntity)
                    .toList();
        }

        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .subCategorias(subCategorias)
                .build();
    }
}
