package com.main.skillexchangeapi.domain.entities;

import java.util.UUID;

public class InfoAppTransaccion extends InfoFinanciera {
    private String codUsuario;
    private String medio;

    InfoAppTransaccion(UUID id, Usuario usuario, String clave) {
        super(id, usuario, clave);
    }
}
