package com.main.skillexchangeapi.app.requests.servicio;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;

import lombok.Getter;

@Getter
public class ModalidadPagoBody {
    private ModalidadPagoConstants.Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
}
