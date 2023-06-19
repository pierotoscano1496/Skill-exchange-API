package com.main.skillexchangeapi.domain.entities;

public class InfoAppTransaccion extends InfoFinanciera {
    private String codUsuario;
    private String medio;

    public InfoAppTransaccion(Long id) {
        super(id);
    }
}
