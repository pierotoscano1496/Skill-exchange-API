package com.main.skillexchangeapi.domain.entities.detail;

import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;

public class SkillUsuario {
    private Long idUsuario;
    private Long idSkill;
    private int nivelConocimiento;
    private Usuario usuario;
    private Skill skill;
    private String descripcion;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }

    public int getNivelConocimiento() {
        return nivelConocimiento;
    }

    public void setNivelConocimiento(int nivelConocimiento) {
        this.nivelConocimiento = nivelConocimiento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
