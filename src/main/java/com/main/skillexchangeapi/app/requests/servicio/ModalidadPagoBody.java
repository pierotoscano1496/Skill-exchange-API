package com.main.skillexchangeapi.app.requests.servicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModalidadPagoBody {
    private UUID id;
    private ModalidadPagoConstants.Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
}
