package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;

@Data
@Builder
public class ModalidadPagoAsignado {
    private UUID id;
    private Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
}
