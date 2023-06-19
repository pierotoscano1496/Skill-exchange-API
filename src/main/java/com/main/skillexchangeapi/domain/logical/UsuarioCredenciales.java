package com.main.skillexchangeapi.domain.logical;

public class UsuarioCredenciales {
    private String email;
    private String clave; // Entrada de clave:

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
