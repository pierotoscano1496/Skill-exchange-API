package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.entities.SubCategoria;

@Data
@Builder
public class SubCategoriaResponse {
    private UUID id;
    private String nombre;
    private UUID idCategoria;
    private CategoriaResponse categoria;
    private List<SkillResponse> skills;

    public static SubCategoriaResponse fromEntity(SubCategoria subCategoria) {
        if (subCategoria == null) {
            return null;
        }

        List<SkillResponse> skills = null;

        if (subCategoria.getSkills() != null && !subCategoria.getSkills().isEmpty()) {
            skills = subCategoria.getSkills().stream()
                    .map(SkillResponse::fromEntity)
                    .toList();
        }

        return SubCategoriaResponse.builder()
                .id(subCategoria.getId())
                .nombre(subCategoria.getNombre())
                .idCategoria(subCategoria.getCategoria() != null ? subCategoria.getCategoria().getId() : null)
                .categoria(CategoriaResponse.fromEntity(subCategoria.getCategoria()))
                .skills(skills)
                .build();
    }
}
