package com.main.skillexchangeapi.domain.entities.detail;

import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Usuario;

public class PlanUsuario {
    private Plan plan;
    private Usuario usuario;
    private double monto;
    private String moneda;
    private boolean isActive;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
