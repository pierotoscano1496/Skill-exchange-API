package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.SubCategoria;

@Data
@Builder
public class SkillResponse {
    private UUID id;
    private String descripcion;
    private UUID idSubCategoria;
    private SubCategoriaResponse subCategoria;

    public static SkillResponse fromEntity(Skill skill) {
        if (skill == null) {
            return null;
        }

        SubCategoria subCategoria = skill.getSubCategoria();

        return SkillResponse.builder()
                .id(skill.getId())
                .descripcion(skill.getDescripcion())
                .idSubCategoria(subCategoria != null ? subCategoria.getId() : null)
                .subCategoria(SubCategoriaResponse.fromEntity(skill.getSubCategoria()))
                .build();
    }
}
