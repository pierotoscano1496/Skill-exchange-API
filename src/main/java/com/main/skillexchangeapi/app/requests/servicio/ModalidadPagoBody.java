package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

@Getter
public class ModalidadPagoBody {
    /**
     * Valores posibles: "yape", "tarjeta", "linea", "efectivo"
     */
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
}
