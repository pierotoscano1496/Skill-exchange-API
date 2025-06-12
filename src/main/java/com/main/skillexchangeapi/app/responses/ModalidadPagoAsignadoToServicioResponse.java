package com.main.skillexchangeapi.app.responses;

import lombok.Builder;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;

@Builder
public class ModalidadPagoAsignadoToServicioResponse {
    private UUID id;
    private Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private UUID idServicio;
    private String url;
}
