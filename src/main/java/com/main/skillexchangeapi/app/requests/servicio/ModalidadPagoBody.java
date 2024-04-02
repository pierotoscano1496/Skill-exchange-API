package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

@Getter
public class ModalidadPagoBody {
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
}
