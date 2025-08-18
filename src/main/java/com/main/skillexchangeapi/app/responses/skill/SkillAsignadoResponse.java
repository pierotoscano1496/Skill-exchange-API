package com.main.skillexchangeapi.app.responses.skill;

import java.util.UUID;

import com.google.auto.value.AutoValue.Builder;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SkillAsignadoResponse extends SkillInfoResponse {
    private int nivelConocimiento;
    private String descripcionDesempeno;

    /*
     * public SkillAsignadoResponse(UUID id, String descripcion, String
     * nombreSubCategoria, String nombreCategoria,
     * int nivelConocimiento, String descripcionDesempeno) {
     * super(id, descripcion, nombreSubCategoria, nombreCategoria);
     * this.nivelConocimiento = nivelConocimiento;
     * this.descripcionDesempeno = descripcionDesempeno;
     * }
     */
}
