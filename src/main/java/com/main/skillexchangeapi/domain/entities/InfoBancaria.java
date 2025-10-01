package com.main.skillexchangeapi.domain.entities;

import java.util.UUID;

public class InfoBancaria extends InfoFinanciera {
    private String codInterbancario;
    private String numTarjeta;
    private String banco;

    InfoBancaria(UUID id, Usuario usuario, String clave) {
        super(id, usuario, clave);
    }
}
