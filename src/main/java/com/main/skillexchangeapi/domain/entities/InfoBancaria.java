package com.main.skillexchangeapi.domain.entities;

public class InfoBancaria extends InfoFinanciera {
    private String codInterbancario;
    private String numTarjeta;
    private String banco;

    public InfoBancaria(Long id) {
        super(id);
    }
}
