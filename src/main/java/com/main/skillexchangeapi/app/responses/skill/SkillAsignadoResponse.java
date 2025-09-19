package com.main.skillexchangeapi.app.responses.skill;

import java.util.UUID;

import org.checkerframework.checker.units.qual.s;

import com.google.auto.value.AutoValue.Builder;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SkillAsignadoResponse extends SkillInfoResponse {
    private int nivelConocimiento;
    private String descripcionDesempeno;

    public static SkillAsignadoResponse fromEntity(SkillUsuario skillUsuario) {
        if (skillUsuario == null) {
            return null;
        }

        String nombreSubCategoria = skillUsuario.getSkill() != null && skillUsuario.getSkill().getSubCategoria() != null
                ? skillUsuario.getSkill().getSubCategoria().getNombre()
                : null;

        String nombreCategoria = skillUsuario.getSkill() != null
                && skillUsuario.getSkill().getSubCategoria() != null
                && skillUsuario.getSkill().getSubCategoria().getCategoria() != null
                        ? skillUsuario.getSkill().getSubCategoria().getCategoria().getNombre()
                        : null;

        return SkillAsignadoResponse.builder()
                .id(skillUsuario.getSkill().getId())
                .descripcion(skillUsuario.getDescripcion())
                .nombreSubCategoria(nombreSubCategoria)
                .nombreCategoria(nombreCategoria)
                .nivelConocimiento(skillUsuario.getNivelConocimiento())
                .descripcionDesempeno(skillUsuario.getDescripcion())
                .build();
    }
}
