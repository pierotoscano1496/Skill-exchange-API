package com.main.skillexchangeapi.domain.logical;

import com.main.skillexchangeapi.domain.entities.Usuario;

public class UsuarioContentRegister {
    private Usuario usuario;
    private String clave; // Recibir la clave hasheada

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
