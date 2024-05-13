package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

@Getter
public class AsignacionModalidadPagoToServicioRequest {
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
}
