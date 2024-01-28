package com.main.skillexchangeapi.app.requests;

import lombok.Getter;

@Getter
public class AsignacionModalidadPagoToServicioRequest {
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
}
