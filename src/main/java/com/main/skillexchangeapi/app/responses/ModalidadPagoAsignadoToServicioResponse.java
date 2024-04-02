package com.main.skillexchangeapi.app.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public class ModalidadPagoAsignadoToServicioResponse {
    private UUID id;
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private UUID idServicio;
}
